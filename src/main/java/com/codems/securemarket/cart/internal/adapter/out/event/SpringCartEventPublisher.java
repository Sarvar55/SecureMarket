package com.codems.securemarket.cart.internal.adapter.out.event;

import com.codems.securemarket.cart.internal.application.port.out.CartEventPublisherPort;
import com.codems.securemarket.shared.event.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class SpringCartEventPublisher implements CartEventPublisherPort {
    private final ApplicationEventPublisher publisher;

    SpringCartEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }
}
