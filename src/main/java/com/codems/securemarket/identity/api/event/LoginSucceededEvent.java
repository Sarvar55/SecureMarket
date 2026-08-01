package com.codems.securemarket.identity.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record LoginSucceededEvent(
        UUID eventId,
        Long userId,
        Instant occurredAt
) implements DomainEvent {
}
