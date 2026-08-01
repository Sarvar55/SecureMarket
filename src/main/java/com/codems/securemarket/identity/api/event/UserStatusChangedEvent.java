package com.codems.securemarket.identity.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record UserStatusChangedEvent(
        UUID eventId,
        Long actorId,
        Long userId,
        String previousStatus,
        String currentStatus,
        Instant occurredAt
) implements DomainEvent {
}
