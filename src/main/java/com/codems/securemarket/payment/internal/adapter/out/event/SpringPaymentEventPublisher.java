package com.codems.securemarket.payment.internal.adapter.out.event;

import com.codems.securemarket.payment.internal.application.port.out.PaymentEventPublisherPort;
import com.codems.securemarket.shared.event.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class SpringPaymentEventPublisher implements PaymentEventPublisherPort {

    private final ApplicationEventPublisher eventPublisher;

    SpringPaymentEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publish(DomainEvent event) {
        eventPublisher.publishEvent(event);
    }
}

