package sharedcontracts.sales.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

// shared contract / published language
public class OrderPlacedEvent {

	private final String orderId;
	private final String customerId;
	private final List<OrderItemPayload> items;
	private final BigDecimal totalAmount;
	private final Instant occuredOn;
	
	public OrderPlacedEvent(
			final String orderId, 
			final String customerId, 
			final List<OrderItemPayload> items, 
			final BigDecimal totalAmount,
			final Instant occuredOn) {
		
		this.orderId = orderId;
		this.customerId = customerId;
		this.items = items;
		this.totalAmount = totalAmount;
		this.occuredOn = occuredOn;
	}

	public String getOrderId() {
		return orderId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public List<OrderItemPayload> getItems() {
		return items;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public Instant getOccuredOn() {
		return occuredOn;
	}
	
}
