package com.codems.securemarket.order.internal.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.codems.securemarket.order.internal.domain.exception.MixedCurrencyException;
import com.codems.securemarket.order.internal.domain.exception.InvalidOrderStatusTransitionException;
import com.codems.securemarket.shared.domain.Money;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderTest {
    private static final Instant NOW = Instant.parse("2026-08-01T10:00:00Z");

    @Test
    void calculatesAndFreezesCheckoutTotal() {
        Order order = Order.create(1L, List.of(
                item(10L, "10.00", "AZN", 2),
                item(20L, "5.50", "AZN", 1)
        ), NOW);

        assertThat(order.getTotal().amount()).isEqualByComparingTo("25.50");
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING_PAYMENT);
    }

    @Test
    void rejectsMixedCurrencies() {
        assertThatThrownBy(() -> Order.create(1L, List.of(
                item(10L, "10.00", "AZN", 1),
                item(20L, "5.00", "USD", 1)
        ), NOW)).isInstanceOf(MixedCurrencyException.class);
    }

    @Test
    void paymentResultIsIdempotent() {
        Order order = Order.create(1L, List.of(item(10L, "10.00", "AZN", 1)), NOW);

        order.markPaid(NOW.plusSeconds(1));
        order.markPaymentFailed(NOW.plusSeconds(2));

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void advancesFulfillmentInRequiredOrder() {
        Order order = Order.create(1L, List.of(item(10L, "10.00", "AZN", 1)), NOW);
        order.markPaid(NOW.plusSeconds(1));

        order.advanceFulfillment(OrderStatus.PROCESSING, NOW.plusSeconds(2));
        order.advanceFulfillment(OrderStatus.SHIPPED, NOW.plusSeconds(3));
        order.advanceFulfillment(OrderStatus.DELIVERED, NOW.plusSeconds(4));

        assertThat(order.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    void rejectsSkippedFulfillmentStatus() {
        Order order = Order.create(1L, List.of(item(10L, "10.00", "AZN", 1)), NOW);
        order.markPaid(NOW.plusSeconds(1));

        assertThatThrownBy(() -> order.advanceFulfillment(
                OrderStatus.SHIPPED, NOW.plusSeconds(2)
        )).isInstanceOf(InvalidOrderStatusTransitionException.class);
    }

    private OrderItem item(Long productId, String amount, String currency, int quantity) {
        return new OrderItem(
                productId, "Product " + productId,
                new Money(new BigDecimal(amount), currency), quantity
        );
    }
}
