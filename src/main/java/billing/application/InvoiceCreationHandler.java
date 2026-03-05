package billing.application;

import sharedcontracts.sales.event.OrderPlacedEvent;

// consumer event
public class InvoiceCreationHandler {

	private final InvoiceService invoiceService;
	
	public InvoiceCreationHandler(
			final InvoiceService invoiceService) {
		this.invoiceService = invoiceService;
	}
	
	public void handle(
			final OrderPlacedEvent event) {
		invoiceService.create(
				event.getOrderId(),
				event.getCustomerId(),
				event.getTotalAmount()
		);
	}
	
}
