package sales.application;

import sales.domain.Order;
import sales.domain.OrderId;
import sales.domain.port.OrderRepository;
import sharedcontracts.sales.event.OrderPlacedEvent;


// publishes event
public class ConfirmOrderUseCase {

	private final OrderRepository orderRepository;
	private final OrderEventMapper eventMapper;
	private final EventPublisher eventPublisher;
	
	public ConfirmOrderUseCase(
			final OrderRepository orderRepository, 
			final OrderEventMapper eventMapper,
			final EventPublisher eventPublisher) {
		
		this.orderRepository = orderRepository;
		this.eventMapper = eventMapper;
		this.eventPublisher = eventPublisher;
	}
	
	public void execute(
			final OrderId id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Order not found"));
		
		order.confirm();
		orderRepository.save(order);
		
		OrderPlacedEvent event = eventMapper.toPublishedEvent(order);
		eventPublisher.publish(event);
	} 
	
}
