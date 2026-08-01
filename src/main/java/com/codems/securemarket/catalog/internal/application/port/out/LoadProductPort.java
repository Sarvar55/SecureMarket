package com.codems.securemarket.catalog.internal.application.port.out;

import com.codems.securemarket.catalog.internal.domain.model.Product;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LoadProductPort {

    Optional<Product> findById(Long productId);

    Optional<Product> findPublicById(Long productId);

    boolean existsBySku(String sku);

    List<Product> findAllPublic();

    List<Product> findAllByIds(Set<Long> productIds);
}

