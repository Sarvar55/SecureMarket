package com.codems.securemarket.catalog.api.event;

import com.codems.securemarket.shared.domain.Money;
import com.codems.securemarket.shared.event.DomainEvent;
import java.time.Instant;
import java.util.UUID;

public record ProductPriceChangedEvent(
        UUID eventId,
        Long productId,
        Money oldPrice,
        Money newPrice,
        Long actorId,
        Instant occurredAt
) implements DomainEvent {
}
