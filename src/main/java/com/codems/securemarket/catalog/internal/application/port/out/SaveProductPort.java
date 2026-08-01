package com.codems.securemarket.catalog.internal.application.port.out;

import com.codems.securemarket.catalog.internal.domain.model.Product;

public interface SaveProductPort {

    Product save(Product product);
}

