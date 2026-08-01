package com.codems.securemarket.order.internal.application.port.out;

import com.codems.securemarket.shared.event.DomainEvent;

public interface OrderEventPublisherPort {
    void publish(DomainEvent event);
}
