package com.codems.securemarket.cart.internal.application.port.out;

import com.codems.securemarket.shared.event.DomainEvent;

public interface CartEventPublisherPort {
    void publish(DomainEvent event);
}
