package sales.application;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import sales.domain.Order;
import sales.domain.OrderItem;
import sharedcontracts.sales.event.OrderItemPayload;
import sharedcontracts.sales.event.OrderPlacedEvent;

public class OrderEventMapper {

	public OrderPlacedEvent toPublishedEvent(
			final Order order) {
		return new OrderPlacedEvent(
				order.getId().getValue(), 
				order.getCustomerId().getValue(),
				toItemsPayload(order.getItems()),
				order.totalAmount(),
				Instant.now());
	}

	private List<OrderItemPayload> toItemsPayload(
			final List<OrderItem> items) {
		List<OrderItemPayload> data = new ArrayList<>();
		items.forEach(e -> data.add(new OrderItemPayload(
				e.getProductId(), 
				e.getQuantity(), 
				e.getPrice())));
		return data;
	}
	
}
