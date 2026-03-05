package sales.domain;

import java.math.BigDecimal;
import java.util.List;

public class Order {

	private OrderId id;
	private CustomerId customerId;
	private List<OrderItem> items;
	private OrderStatus status;
	
	public void confirm() {
		if(status != OrderStatus.PENDING) {
			String msg = "Invalid order state";
			throw new IllegalStateException(msg);
		}
		this.status = OrderStatus.CONFIRMED;
	}
	
	public BigDecimal totalAmount() {
		return items.stream().map(OrderItem::subtotal)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public OrderId getId() {
		return id;
	}
	
	public CustomerId getCustomerId() {
		return customerId;
	}
	
	public List<OrderItem> getItems() {
		return List.copyOf(items);
	}
	
	
}
