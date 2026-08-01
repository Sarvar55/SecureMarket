package com.codems.securemarket.catalog.internal.application.port.in.query;

import com.codems.securemarket.catalog.internal.domain.model.Category;
import java.io.Serializable;

public record CategoryView(
        Long id,
        String name,
        String slug,
        boolean active
) implements Serializable {
    public static CategoryView from(Category category) {
        return new CategoryView(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.isActive()
        );
    }
}

