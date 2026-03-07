# Published Language | Ejemplo de Patrón Estratégico de DDD

[![Pattern](https://img.youtube.com/vi/pNCXeyShiJY/0.jpg)](https://www.youtube.com/watch?v=pNCXeyShiJY)

## Qué es Published Language?

Published Language es un patrón estratégico de Domain-Driven Design que define un formato de datos compartido y bien documentado (schema) utilizado como contrato de comunicación entre Bounded Contexts. En lugar de permitir que cada contexto interprete o traduzca la información de forma ad hoc, los equipos involucrados acuerdan un lenguaje común — típicamente expresado como schemas de eventos, DTOs o formatos de mensajes — que se versiona, se mantiene y se trata como un artefacto de primera clase en la arquitectura.

Eric Evans lo describe así:

> *"La traducción entre los modelos de dos Bounded Contexts requiere un lenguaje común. Use un lenguaje compartido bien documentado que pueda expresar la información de dominio necesaria como medio común de comunicación, traduciendo según sea necesario hacia y desde ese lenguaje."*
> — Eric Evans, Domain-Driven Design: Tackling Complexity in the Heart of Software

### Por qué es importante

En sistemas distribuidos, los Bounded Contexts necesitan comunicarse sin generar acoplamiento fuerte. Sin Published Language, cada consumidor necesitaría conocimiento íntimo del modelo interno del productor, generando integraciones frágiles que se rompen cuando el productor evoluciona. Published Language resuelve esto estableciendo un contrato explícito:

- **Desacoplamiento**: Los consumidores dependen del contrato publicado, no del modelo de dominio interno del productor. El productor es libre de refactorizar sus internos sin romper los contextos downstream.
- **Contrato explícito**: El schema actúa como un acuerdo formal. Los cambios al contrato son deliberados, versionados y comunicados — no efectos secundarios accidentales de refactorizaciones internas.
- **Autonomía**: Cada Bounded Context mantiene su propio modelo de dominio. El Published Language es una capa de traducción, no un modelo de dominio compartido. Los contextos traducen desde/hacia el formato publicado en sus fronteras.
- **Evolucionabilidad**: Como el contrato es explícito, puede ser versionado y evolucionado de forma independiente usando estrategias como adiciones backward-compatible, versionado de schemas o streams de eventos paralelos.

### Cómo se diferencia de Shared Kernel

Aunque ambos patrones involucran artefactos compartidos, la intención es fundamentalmente diferente:

| Aspecto | Published Language | Shared Kernel |
|---|---|---|
| **Propósito** | Contrato de comunicación entre contextos | Subconjunto compartido del modelo de dominio |
| **Propiedad** | El productor lo define, los consumidores dependen de él | Co-propiedad de múltiples equipos |
| **Acoplamiento** | Bajo — solo se comparte el schema | Mayor — se comparte código y modelo |
| **Evolución** | El productor evoluciona el contrato con versionado | Requiere coordinación entre todos los propietarios |

### Cuándo usar Published Language

- Cuando se integran Bounded Contexts a través de eventos o mensajes asincrónicos.
- Cuando el contexto productor necesita libertad para evolucionar su modelo interno.
- Cuando múltiples consumidores necesitan la misma información pero la interpretan de forma diferente dentro de sus propios modelos.
- Cuando se quiere hacer los contratos de integración explícitos y versionables.

## Sobre este ejemplo

Este proyecto demuestra Published Language a través de un escenario de e-commerce donde tres Bounded Contexts interactúan a través de un contrato de eventos compartido.

### Bounded Contexts

```
sales/                          --> Contexto productor (upstream)
  domain/
    Order.java                  --> Aggregate root
    OrderItem.java              --> Entidad
    OrderId.java                --> Value Object
    CustomerId.java             --> Value Object
    OrderStatus.java            --> Enum (PENDING, CONFIRMED)
    port/
      OrderRepository.java      --> Puerto del repositorio
  application/
    ConfirmOrderUseCase.java    --> Caso de uso que confirma una orden y publica el evento
    OrderEventMapper.java       --> Traduce el modelo de dominio al published language
    EventPublisher.java         --> Puerto para publicar eventos

sharedcontracts/                --> Published Language (el contrato compartido)
  sales/event/
    OrderPlacedEvent.java       --> Schema del evento acordado por todos los contextos
    OrderItemPayload.java       --> DTO de payload dentro del evento

billing/                        --> Contexto consumidor (downstream)
  application/
    InvoiceCreationHandler.java --> Maneja OrderPlacedEvent para crear facturas
    InvoiceService.java         --> Servicio de dominio de facturación

shipping/                       --> Contexto consumidor (downstream)
  application/
    ShippingPreparationHandler.java --> Maneja OrderPlacedEvent para preparar envíos
    ShippingService.java            --> Servicio de dominio de envíos
```

### Cómo funciona

1. **Sales** confirma una orden a través de `ConfirmOrderUseCase`. La orden transiciona de `PENDING` a `CONFIRMED`.
2. `OrderEventMapper` traduce el agregado interno `Order` a un `OrderPlacedEvent` — esta es la traducción del modelo de dominio hacia el Published Language. Nótese cómo convierte Value Objects de dominio (`OrderId`, `CustomerId`) en tipos primitivos (`String`) y entidades de dominio (`OrderItem`) en payloads planos (`OrderItemPayload`).
3. El evento se publica a través de `EventPublisher`.
4. **Billing** recibe el `OrderPlacedEvent` y usa solo los campos que necesita (`orderId`, `customerId`, `totalAmount`) para crear una factura.
5. **Shipping** recibe el mismo evento y usa campos diferentes (`orderId`, `items`) para preparar el envío.

### Decisiones de diseño clave

- **El contrato vive en su propio paquete** (`sharedcontracts`): no es parte del dominio de ningún Bounded Context. Esto hace la frontera explícita — el Published Language es un artefacto de traducción, no un concepto de dominio.
- **El productor traduce hacia afuera**: `OrderEventMapper` vive en la capa de aplicación y convierte el modelo de dominio rico en el evento publicado plano. El modelo de dominio nunca se filtra.
- **Los consumidores dependen solo del contrato**: `InvoiceCreationHandler` y `ShippingPreparationHandler` importan desde `sharedcontracts`, nunca desde `sales.domain`. Cada consumidor toma solo los campos relevantes para su propio contexto.
- **Primitivos en el contrato**: El evento usa `String`, `BigDecimal` e `Instant` — sin tipos específicos de dominio. Esto mantiene el contrato agnóstico a la tecnología y amigable para la serialización.
