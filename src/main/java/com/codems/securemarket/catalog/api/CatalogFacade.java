package com.codems.securemarket.catalog.api;

import java.util.List;
import java.util.Set;

public interface CatalogFacade {

    List<ProductSnapshot> getProductsForCheckout(Set<Long> productIds);

    void decreaseStock(List<StockRequest> requests);

    void increaseStock(List<StockRequest> requests);
}
