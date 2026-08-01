package com.codems.securemarket.identity.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record LoginFailedEvent(
        UUID eventId,
        String attemptedEmail,
        Instant occurredAt
) implements DomainEvent {
}
