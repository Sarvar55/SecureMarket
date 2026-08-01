package com.codems.securemarket.cart.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record CartClearedEvent(
        UUID eventId,
        Long customerId,
        Instant occurredAt
) implements DomainEvent {
}
