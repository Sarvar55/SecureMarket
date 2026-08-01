package com.codems.securemarket.catalog.internal.domain.model;

import com.codems.securemarket.catalog.internal.domain.exception.InsufficientStockException;
import com.codems.securemarket.catalog.internal.domain.exception.InvalidProductPriceException;
import com.codems.securemarket.catalog.internal.domain.exception.InvalidStockQuantityException;
import com.codems.securemarket.shared.domain.Money;
import java.io.Serializable;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;

public final class Product implements Serializable {

    private final Long id;
    private final Long categoryId;
    private final String sku;
    private String name;
    private String description;
    private Money unitPrice;
    private int stock;
    private ProductStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private Product(
            Long id,
            Long categoryId,
            String sku,
            String name,
            String description,
            Money unitPrice,
            int stock,
            ProductStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.categoryId = Objects.requireNonNull(categoryId);
        this.sku = requireText(sku, "sku").toUpperCase(Locale.ROOT);
        this.name = requireText(name, "name");
        this.description = description == null ? null : description.trim();
        this.unitPrice = requirePositivePrice(unitPrice);
        this.stock = requireStock(stock);
        this.status = Objects.requireNonNull(status);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static Product create(
            Long categoryId,
            String sku,
            String name,
            String description,
            Money unitPrice,
            int stock,
            Instant now
    ) {
        return new Product(
                null,
                categoryId,
                sku,
                name,
                description,
                unitPrice,
                stock,
                ProductStatus.INACTIVE,
                now,
                now
        );
    }

    public static Product restore(
            Long id,
            Long categoryId,
            String sku,
            String name,
            String description,
            Money unitPrice,
            int stock,
            ProductStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
        return new Product(
                Objects.requireNonNull(id),
                categoryId,
                sku,
                name,
                description,
                unitPrice,
                stock,
                status,
                createdAt,
                updatedAt
        );
    }

    public void changePrice(Money newPrice, Instant now) {
        unitPrice = requirePositivePrice(newPrice);
        updatedAt = Objects.requireNonNull(now);
    }

    public void adjustStock(int quantity, Instant now) {
        if (quantity == 0) {
            throw new InvalidStockQuantityException();
        }
        if (stock + quantity < 0) {
            throw new InsufficientStockException(id);
        }

        stock += quantity;
        updatedAt = Objects.requireNonNull(now);
    }

    public void changeStatus(ProductStatus newStatus, Instant now) {
        status = Objects.requireNonNull(newStatus);
        updatedAt = Objects.requireNonNull(now);
    }

    public boolean isAvailable() {
        return status == ProductStatus.ACTIVE && stock > 0;
    }

    private static Money requirePositivePrice(Money price) {
        if (price == null || !price.isPositive()) {
            throw new InvalidProductPriceException();
        }
        return price;
    }

    private static int requireStock(int stock) {
        if (stock < 0) {
            throw new InvalidStockQuantityException();
        }
        return stock;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public int getStock() {
        return stock;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
