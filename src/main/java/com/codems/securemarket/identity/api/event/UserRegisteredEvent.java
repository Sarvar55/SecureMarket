package com.codems.securemarket.identity.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID eventId,
        Long userId,
        String email,
        Instant occurredAt
) implements DomainEvent {
}
