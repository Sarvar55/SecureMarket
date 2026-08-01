package com.codems.securemarket.catalog.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record ProductStockChangedEvent(
        UUID eventId,
        Long productId,
        int stock,
        Long actorId,
        String reason,
        Instant occurredAt
) implements DomainEvent {
}
