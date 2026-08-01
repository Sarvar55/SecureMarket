package com.codems.securemarket.catalog.internal.application.service;

import com.codems.securemarket.catalog.api.event.ProductCreatedEvent;
import com.codems.securemarket.catalog.api.event.ProductPriceChangedEvent;
import com.codems.securemarket.catalog.api.event.ProductStatusChangedEvent;
import com.codems.securemarket.catalog.api.event.ProductStockChangedEvent;
import com.codems.securemarket.catalog.internal.application.port.in.ManageProductUseCase;
import com.codems.securemarket.catalog.internal.application.port.in.query.ProductView;
import com.codems.securemarket.catalog.internal.application.port.in.command.AdjustProductStockCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.ChangeProductPriceCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.ChangeProductStatusCommand;
import com.codems.securemarket.catalog.internal.application.port.in.command.CreateProductCommand;
import com.codems.securemarket.catalog.internal.application.port.out.CatalogEventPublisherPort;
import com.codems.securemarket.catalog.internal.application.port.out.LoadCategoryPort;
import com.codems.securemarket.catalog.internal.application.port.out.LoadProductPort;
import com.codems.securemarket.catalog.internal.application.port.out.SaveProductPort;
import com.codems.securemarket.catalog.internal.domain.exception.CategoryNotFoundException;
import com.codems.securemarket.catalog.internal.domain.exception.DuplicateSkuException;
import com.codems.securemarket.catalog.internal.domain.exception.InactiveCategoryException;
import com.codems.securemarket.catalog.internal.domain.exception.ProductNotFoundException;
import com.codems.securemarket.catalog.internal.domain.model.Category;
import com.codems.securemarket.catalog.internal.domain.model.Product;
import com.codems.securemarket.catalog.internal.domain.model.ProductStatus;
import java.time.Clock;
import java.util.UUID;

public final class ProductManagementService implements ManageProductUseCase {

    private final LoadCategoryPort loadCategoryPort;
    private final LoadProductPort loadProductPort;
    private final SaveProductPort saveProductPort;
    private final CatalogEventPublisherPort eventPublisher;
    private final Clock clock;

    public ProductManagementService(
            LoadCategoryPort loadCategoryPort,
            LoadProductPort loadProductPort,
            SaveProductPort saveProductPort,
            CatalogEventPublisherPort eventPublisher,
            Clock clock) {
        this.loadCategoryPort = loadCategoryPort;
        this.loadProductPort = loadProductPort;
        this.saveProductPort = saveProductPort;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    public ProductView create(CreateProductCommand command) {
        requireActiveCategory(command.categoryId());

        if (loadProductPort.existsBySku(command.sku().trim().toUpperCase())) {
            throw new DuplicateSkuException();
        }

        var now = clock.instant();
        Product saved = saveProductPort.save(Product.create(
                command.categoryId(),
                command.sku(),
                command.name(),
                command.description(),
                command.unitPrice(),
                command.stock(),
                now));

        eventPublisher.publish(
                new ProductCreatedEvent(
                        UUID.randomUUID(),
                        saved.getId(),
                        saved.getCategoryId(),
                        command.actorId(),
                        now));
        return ProductView.from(saved);
    }

    @Override
    public ProductView changePrice(ChangeProductPriceCommand command) {
        Product product = loadProduct(command.productId());
        var oldPrice = product.getUnitPrice();
        var now = clock.instant();

        product.changePrice(command.unitPrice(), now);
        Product saved = saveProductPort.save(product);

        eventPublisher.publish(new ProductPriceChangedEvent(
                UUID.randomUUID(),
                saved.getId(),
                oldPrice,
                saved.getUnitPrice(),
                command.actorId(),
                now));
        return ProductView.from(saved);
    }

    @Override
    public ProductView adjustStock(AdjustProductStockCommand command) {
        Product product = loadProduct(command.productId());
        var now = clock.instant();

        product.adjustStock(command.quantity(), now);
        Product saved = saveProductPort.save(product);

        eventPublisher.publish(new ProductStockChangedEvent(
                UUID.randomUUID(),
                saved.getId(),
                saved.getStock(),
                command.actorId(),
                "ADMIN_ADJUSTMENT",
                now));
        return ProductView.from(saved);
    }

    @Override
    public ProductView changeStatus(ChangeProductStatusCommand command) {
        Product product = loadProduct(command.productId());

        if (command.status() == ProductStatus.ACTIVE) {
            requireActiveCategory(product.getCategoryId());
        }

        var now = clock.instant();
        product.changeStatus(command.status(), now);
        Product saved = saveProductPort.save(product);

        eventPublisher.publish(new ProductStatusChangedEvent(
                UUID.randomUUID(),
                saved.getId(),
                saved.getStatus().name(),
                command.actorId(),
                now));
        return ProductView.from(saved);
    }

    private Product loadProduct(Long productId) {
        return loadProductPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    private Category requireActiveCategory(Long categoryId) {
        Category category = loadCategoryPort.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        if (!category.isActive()) {
            throw new InactiveCategoryException();
        }
        return category;
    }
}
