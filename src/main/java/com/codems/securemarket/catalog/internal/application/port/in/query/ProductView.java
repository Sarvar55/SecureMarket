package com.codems.securemarket.catalog.internal.application.port.in.query;

import com.codems.securemarket.catalog.internal.domain.model.Product;
import com.codems.securemarket.shared.domain.Money;
import java.io.Serializable;

public record ProductView(
        Long id,
        Long categoryId,
        String sku,
        String name,
        String description,
        Money unitPrice,
        int stock,
        boolean available
) implements Serializable {
    public static ProductView from(Product product) {
        return new ProductView(
                product.getId(),
                product.getCategoryId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                product.getStock(),
                product.isAvailable()
        );
    }
}

