package sharedcontracts.sales.event;

import java.math.BigDecimal;

public class OrderItemPayload {

	private final String productId;
	private final int quantity;
	private final BigDecimal price;
	
	public OrderItemPayload(
			final String productId, 
			final int quantity, 
			final BigDecimal price) {
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}
	
	public String getProductId() {
		return productId;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
}
