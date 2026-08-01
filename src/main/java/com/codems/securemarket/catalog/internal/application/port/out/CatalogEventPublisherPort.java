package com.codems.securemarket.catalog.internal.application.port.out;

import com.codems.securemarket.shared.event.DomainEvent;

public interface CatalogEventPublisherPort {

    void publish(DomainEvent event);
}

