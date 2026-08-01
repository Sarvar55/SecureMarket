package com.codems.securemarket.catalog.internal.adapter.in.facade;

import com.codems.securemarket.catalog.api.CatalogFacade;
import com.codems.securemarket.catalog.api.ProductSnapshot;
import com.codems.securemarket.catalog.api.StockRequest;
import com.codems.securemarket.catalog.internal.application.service.CatalogCheckoutService;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class CatalogFacadeAdapter implements CatalogFacade {

    private final CatalogCheckoutService service;

    CatalogFacadeAdapter(CatalogCheckoutService service) {
        this.service = service;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductSnapshot> getProductsForCheckout(Set<Long> productIds) {
        return service.getProductsForCheckout(productIds);
    }

    @Override
    @Transactional
    public void decreaseStock(List<StockRequest> requests) {
        service.decreaseStock(requests);
    }

    @Override
    @Transactional
    public void increaseStock(List<StockRequest> requests) {
        service.increaseStock(requests);
    }
}
