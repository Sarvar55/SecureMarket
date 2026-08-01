package com.codems.securemarket.catalog.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record ProductStatusChangedEvent(
        UUID eventId,
        Long productId,
        String status,
        Long actorId,
        Instant occurredAt
) implements DomainEvent {
}
