package com.codems.securemarket.catalog.internal.application.service;

import com.codems.securemarket.catalog.internal.application.port.in.query.CategoryView;
import com.codems.securemarket.catalog.internal.application.port.in.query.ProductView;
import com.codems.securemarket.catalog.internal.application.port.in.QueryCatalogUseCase;
import com.codems.securemarket.catalog.internal.application.port.out.LoadCategoryPort;
import com.codems.securemarket.catalog.internal.application.port.out.LoadProductPort;
import com.codems.securemarket.catalog.internal.domain.exception.ProductNotFoundException;
import java.util.List;

public final class CatalogQueryService implements QueryCatalogUseCase {

    private final LoadCategoryPort loadCategoryPort;
    private final LoadProductPort loadProductPort;

    public CatalogQueryService(
            LoadCategoryPort loadCategoryPort,
            LoadProductPort loadProductPort) {
        this.loadCategoryPort = loadCategoryPort;
        this.loadProductPort = loadProductPort;
    }

    @Override
    public List<CategoryView> getCategories() {
        return loadCategoryPort.findAllActive()
                .stream()
                .map(CategoryView::from)
                .toList();
    }

    @Override
    public List<ProductView> getProducts() {
        return loadProductPort.findAllPublic()
                .stream()
                .map(ProductView::from)
                .toList();
    }

    @Override
    public ProductView getProduct(Long productId) {
        return loadProductPort.findPublicById(productId)
                .map(ProductView::from)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
