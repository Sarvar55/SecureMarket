package com.codems.securemarket.identity.internal.adapter.out.event;

import com.codems.securemarket.identity.internal.application.port.out.IdentityEventPublisherPort;
import com.codems.securemarket.shared.event.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class SpringIdentityEventPublisher implements IdentityEventPublisherPort {

    private final ApplicationEventPublisher eventPublisher;

    SpringIdentityEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publish(DomainEvent event) {
        log.info("Publishing identity event: {}", event.eventId());
        eventPublisher.publishEvent(event);
    }
}
