package com.codems.securemarket.catalog.internal.application.port.in.command;

import com.codems.securemarket.shared.domain.Money;

public record ChangeProductPriceCommand(Long productId, Money unitPrice, Long actorId) {
}
