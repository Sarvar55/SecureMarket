package com.codems.securemarket.payment.internal.application.port.out;

import com.codems.securemarket.shared.event.DomainEvent;

public interface PaymentEventPublisherPort {

    void publish(DomainEvent event);
}

