package com.codems.securemarket.catalog.api;

import com.codems.securemarket.shared.domain.Money;
import java.io.Serializable;

public record ProductSnapshot(
        Long productId,
        String name,
        Money unitPrice,
        int stock
) implements Serializable {
}

