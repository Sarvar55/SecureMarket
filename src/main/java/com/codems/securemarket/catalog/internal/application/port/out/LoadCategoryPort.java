package com.codems.securemarket.catalog.internal.application.port.out;

import com.codems.securemarket.catalog.internal.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface LoadCategoryPort {

    Optional<Category> findById(Long categoryId);

    boolean existsBySlug(String slug);

    List<Category> findAllActive();
}

