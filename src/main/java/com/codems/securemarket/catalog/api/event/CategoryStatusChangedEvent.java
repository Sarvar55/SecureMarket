package com.codems.securemarket.catalog.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record CategoryStatusChangedEvent(
        UUID eventId,
        Long categoryId,
        boolean active,
        Long actorId,
        Instant occurredAt
) implements DomainEvent {
}
