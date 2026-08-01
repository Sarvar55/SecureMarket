package com.codems.securemarket.catalog.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record ProductCreatedEvent(
        UUID eventId,
        Long productId,
        Long categoryId,
        Long actorId,
        Instant occurredAt
) implements DomainEvent {
}
