package com.codems.securemarket.cart.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record CartItemChangedEvent(
        UUID eventId,
        Long customerId,
        Long productId,
        int quantity,
        Instant occurredAt
) implements DomainEvent {
}
