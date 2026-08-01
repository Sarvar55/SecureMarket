package com.codems.securemarket.cart.internal.adapter.out.catalog;

import com.codems.securemarket.cart.internal.application.port.out.CheckProductQuantityPort;
import com.codems.securemarket.cart.internal.domain.exception.InvalidCartQuantityException;
import com.codems.securemarket.cart.internal.domain.exception.RequestedQuantityUnavailableException;
import com.codems.securemarket.catalog.api.CatalogFacade;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
class CatalogProductQuantityAdapter implements CheckProductQuantityPort {

    private final CatalogFacade catalogFacade;

    CatalogProductQuantityAdapter(CatalogFacade catalogFacade) {
        this.catalogFacade = catalogFacade;
    }

    @Override
    public void check(Long productId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidCartQuantityException();
        }
        var product = catalogFacade.getProductsForCheckout(Set.of(productId)).get(0);
        if (product.stock() < quantity) {
            throw new RequestedQuantityUnavailableException();
        }
    }
}
