package com.codems.securemarket.catalog.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record CategoryCreatedEvent(
        UUID eventId,
        Long categoryId,
        String name,
        Long actorId,
        Instant occurredAt
) implements DomainEvent {
}
