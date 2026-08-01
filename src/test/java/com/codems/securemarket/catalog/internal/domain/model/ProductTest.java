package com.codems.securemarket.catalog.internal.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codems.securemarket.catalog.internal.domain.exception.InsufficientStockException;
import com.codems.securemarket.shared.domain.Money;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class ProductTest {

    private static final Instant NOW = Instant.parse("2026-08-01T10:00:00Z");

    @Test
    void newProductStartsInactive() {
        Product product = productWithStock(5);

        assertThat(product.getStatus()).isEqualTo(ProductStatus.INACTIVE);
        assertThat(product.isAvailable()).isFalse();
    }

    @Test
    void stockCannotBecomeNegative() {
        Product product = productWithStock(2);

        assertThatThrownBy(() -> product.adjustStock(-3, NOW.plusSeconds(1)))
                .isInstanceOf(InsufficientStockException.class);
        assertThat(product.getStock()).isEqualTo(2);
    }

    @Test
    void activeProductWithStockIsAvailable() {
        Product product = productWithStock(2);

        product.changeStatus(ProductStatus.ACTIVE, NOW.plusSeconds(1));

        assertThat(product.isAvailable()).isTrue();
    }

    private Product productWithStock(int stock) {
        return Product.create(
                1L, "SKU-1", "Keyboard", null,
                new Money(new BigDecimal("25.00"), "USD"), stock, NOW
        );
    }
}
