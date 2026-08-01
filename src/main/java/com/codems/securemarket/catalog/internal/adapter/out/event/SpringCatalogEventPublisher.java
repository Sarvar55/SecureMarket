package com.codems.securemarket.catalog.internal.adapter.out.event;

import com.codems.securemarket.catalog.internal.application.port.out.CatalogEventPublisherPort;
import com.codems.securemarket.shared.event.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class SpringCatalogEventPublisher implements CatalogEventPublisherPort {

    private final ApplicationEventPublisher publisher;

    SpringCatalogEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }
}
