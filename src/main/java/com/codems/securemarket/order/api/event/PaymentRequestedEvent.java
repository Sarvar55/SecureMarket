package com.codems.securemarket.order.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentRequestedEvent(
        UUID eventId,
        Long orderId,
        Long customerId,
        BigDecimal amount,
        String currency,
        Instant occurredAt
) implements DomainEvent {
}

