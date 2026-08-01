package com.codems.securemarket.catalog.internal.application.port.in;

import com.codems.securemarket.catalog.internal.application.port.in.query.CategoryView;

import com.codems.securemarket.catalog.internal.application.port.in.query.ProductView;

import java.util.List;

public interface QueryCatalogUseCase {

    List<CategoryView> getCategories();

    List<ProductView> getProducts();

    ProductView getProduct(Long productId);
}

