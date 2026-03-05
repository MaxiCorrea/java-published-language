package sales.application;

import sharedcontracts.sales.event.OrderPlacedEvent;

public interface EventPublisher {

	void publish(OrderPlacedEvent event);
	
}
