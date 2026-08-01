package com.codems.securemarket.shared.event;

import java.time.Instant;
import java.util.UUID;

public record UnexpectedApplicationErrorEvent(
        UUID eventId,
        String exceptionType,
        String httpMethod,
        String requestPath,
        Instant occurredAt
) implements DomainEvent {
}
