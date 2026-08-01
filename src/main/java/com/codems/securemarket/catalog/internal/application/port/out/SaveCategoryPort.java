package com.codems.securemarket.catalog.internal.application.port.out;

import com.codems.securemarket.catalog.internal.domain.model.Category;

public interface SaveCategoryPort {

    Category save(Category category);
}

