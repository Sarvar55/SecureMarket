package com.codems.securemarket.order.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record OrderStatusChangedEvent(
        UUID eventId,
        Long orderId,
        Long customerId,
        String previousStatus,
        String currentStatus,
        Long actorId,
        Instant occurredAt
) implements DomainEvent {
}
