package com.codems.securemarket.order.internal.adapter.out.event;

import com.codems.securemarket.order.internal.application.port.out.OrderEventPublisherPort;
import com.codems.securemarket.shared.event.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class SpringOrderEventPublisher implements OrderEventPublisherPort {
    private final ApplicationEventPublisher publisher;

    SpringOrderEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }
}
