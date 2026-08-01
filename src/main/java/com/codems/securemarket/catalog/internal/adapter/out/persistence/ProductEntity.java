package com.codems.securemarket.catalog.internal.adapter.out.persistence;

import com.codems.securemarket.catalog.internal.domain.model.Product;
import com.codems.securemarket.catalog.internal.domain.model.ProductStatus;
import com.codems.securemarket.shared.domain.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "products")
class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(nullable = false, unique = true, length = 64)
    private String sku;

    @Column(nullable = false, length = 180)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private long version;

    protected ProductEntity() {
    }

    static ProductEntity from(Product product) {
        var entity = new ProductEntity();
        entity.id = product.getId();
        entity.updateFrom(product);
        return entity;
    }

    void updateFrom(Product product) {
        categoryId = product.getCategoryId();
        sku = product.getSku();
        name = product.getName();
        description = product.getDescription();
        unitPrice = product.getUnitPrice().amount();
        currency = product.getUnitPrice().currency();
        stock = product.getStock();
        status = product.getStatus();
        createdAt = product.getCreatedAt();
        updatedAt = product.getUpdatedAt();
    }

    Product toDomain() {
        return Product.restore(
                id, categoryId, sku, name, description,
                new Money(unitPrice, currency), stock, status, createdAt, updatedAt
        );
    }
}
