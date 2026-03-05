package sales.domain.port;

import java.util.Optional;

import sales.domain.Order;
import sales.domain.OrderId;

public interface OrderRepository {

	Optional<Order> findById(OrderId id);

	void save(Order order);
	
}
