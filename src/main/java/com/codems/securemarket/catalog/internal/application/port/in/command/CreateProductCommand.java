package com.codems.securemarket.catalog.internal.application.port.in.command;

import com.codems.securemarket.shared.domain.Money;

public record CreateProductCommand(
        Long categoryId,
        String sku,
        String name,
        String description,
        Money unitPrice,
        int stock,
        Long actorId
) {
}
