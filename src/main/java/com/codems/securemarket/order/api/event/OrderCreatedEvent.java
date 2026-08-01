package com.codems.securemarket.order.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        Long orderId,
        Long customerId,
        Instant occurredAt
) implements DomainEvent {
}
