package com.codems.securemarket.payment.api.event;

import com.codems.securemarket.shared.event.DomainEvent;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentProcessedEvent(
        UUID eventId,
        UUID paymentId,
        Long orderId,
        Long customerId,
        BigDecimal amount,
        String currency,
        PaymentStatus status,
        String failureReason,
        Instant occurredAt
) implements DomainEvent {
}

