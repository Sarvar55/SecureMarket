package com.codems.securemarket.identity.internal.application.port.out;

import com.codems.securemarket.shared.event.DomainEvent;

public interface IdentityEventPublisherPort {

    void publish(DomainEvent event);
}
