package com.codems.securemarket.catalog.internal.application.service;

import com.codems.securemarket.catalog.api.ProductSnapshot;
import com.codems.securemarket.catalog.api.StockRequest;
import com.codems.securemarket.catalog.api.event.ProductStockChangedEvent;
import com.codems.securemarket.catalog.internal.application.port.out.CatalogEventPublisherPort;
import com.codems.securemarket.catalog.internal.application.port.out.DecreaseProductStockPort;
import com.codems.securemarket.catalog.internal.application.port.out.LoadCategoryPort;
import com.codems.securemarket.catalog.internal.application.port.out.LoadProductPort;
import com.codems.securemarket.catalog.internal.application.port.out.SaveProductPort;
import com.codems.securemarket.catalog.internal.domain.exception.InsufficientStockException;
import com.codems.securemarket.catalog.internal.domain.exception.ProductUnavailableException;
import com.codems.securemarket.catalog.internal.domain.model.Product;
import com.codems.securemarket.catalog.internal.domain.model.ProductStatus;
import java.time.Clock;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class CatalogCheckoutService {

    private final LoadProductPort loadProductPort;
    private final LoadCategoryPort loadCategoryPort;
    private final DecreaseProductStockPort decreaseProductStockPort;
    private final SaveProductPort saveProductPort;
    private final CatalogEventPublisherPort eventPublisher;
    private final Clock clock;

    public CatalogCheckoutService(
            LoadProductPort loadProductPort,
            LoadCategoryPort loadCategoryPort,
            DecreaseProductStockPort decreaseProductStockPort,
            SaveProductPort saveProductPort,
            CatalogEventPublisherPort eventPublisher,
            Clock clock
    ) {
        this.loadProductPort = loadProductPort;
        this.loadCategoryPort = loadCategoryPort;
        this.decreaseProductStockPort = decreaseProductStockPort;
        this.saveProductPort = saveProductPort;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    public List<ProductSnapshot> getProductsForCheckout(Set<Long> productIds) {
        List<Product> products = loadProductPort.findAllByIds(productIds);

        if (products.size() != productIds.size()) {
            throw new ProductUnavailableException(null);
        }

        return products.stream()
                .map(this::toCheckoutSnapshot)
                .toList();
    }

    public void decreaseStock(List<StockRequest> requests) {
        for (StockRequest request : requests) {
            if (!decreaseProductStockPort.decrease(
                    request.productId(),
                    request.quantity()
            )) {
                throw new InsufficientStockException(request.productId());
            }

            Product updated = loadProductPort.findById(request.productId())
                    .orElseThrow(() -> new ProductUnavailableException(request.productId()));

            eventPublisher.publish(new ProductStockChangedEvent(
                    UUID.randomUUID(),
                    updated.getId(),
                    updated.getStock(),
                    null,
                    "CHECKOUT",
                    clock.instant()
            ));
        }
    }

    public void increaseStock(List<StockRequest> requests) {
        for (StockRequest request : requests) {
            Product product = loadProductPort.findById(request.productId())
                    .orElseThrow(() -> new ProductUnavailableException(request.productId()));
            var now = clock.instant();
            product.adjustStock(request.quantity(), now);
            Product saved = saveProductPort.save(product);
            eventPublisher.publish(new ProductStockChangedEvent(
                    UUID.randomUUID(), saved.getId(), saved.getStock(), null,
                    "PAYMENT_COMPENSATION", now
            ));
        }
    }

    private ProductSnapshot toCheckoutSnapshot(Product product) {
        boolean categoryActive = loadCategoryPort.findById(product.getCategoryId())
                .map(category -> category.isActive())
                .orElse(false);

        if (product.getStatus() != ProductStatus.ACTIVE
                || product.getStock() <= 0
                || !categoryActive) {
            throw new ProductUnavailableException(product.getId());
        }

        return new ProductSnapshot(
                product.getId(),
                product.getName(),
                product.getUnitPrice(),
                product.getStock()
        );
    }
}
