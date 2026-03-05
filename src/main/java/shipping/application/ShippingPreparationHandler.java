package shipping.application;

import sharedcontracts.sales.event.OrderPlacedEvent;

// consumer event
public class ShippingPreparationHandler {

	private final ShippingService shippingService;
	
	public ShippingPreparationHandler(
			final ShippingService shippingService) {
		this.shippingService = shippingService;
	}
	
	public void handle(
			final OrderPlacedEvent event) {
		shippingService.prepareShipping(
				event.getOrderId(),
				event.getItems()
				);
	}
	
}
