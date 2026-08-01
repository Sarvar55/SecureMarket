package com.codems.securemarket.order.internal.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codems.securemarket.cart.api.CartFacade;
import com.codems.securemarket.cart.api.CartLineSnapshot;
import com.codems.securemarket.cart.api.CartSnapshot;
import com.codems.securemarket.catalog.api.CatalogFacade;
import com.codems.securemarket.catalog.api.ProductSnapshot;
import com.codems.securemarket.order.internal.application.port.out.LoadOrderPort;
import com.codems.securemarket.order.internal.application.port.out.OrderEventPublisherPort;
import com.codems.securemarket.order.internal.application.port.out.SaveOrderPort;
import com.codems.securemarket.order.internal.domain.model.Order;
import com.codems.securemarket.order.internal.domain.model.OrderStatus;
import com.codems.securemarket.shared.domain.Money;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    @Test
    void checkoutCopiesCurrentPriceDecreasesStockAndClearsCart() {
        CartFacade cartFacade = mock(CartFacade.class);
        CatalogFacade catalogFacade = mock(CatalogFacade.class);
        SaveOrderPort saveOrderPort = mock(SaveOrderPort.class);
        var now = Instant.parse("2026-08-01T10:00:00Z");

        when(cartFacade.getCart(7L)).thenReturn(new CartSnapshot(
                7L, List.of(new CartLineSnapshot(10L, 2))
        ));
        when(catalogFacade.getProductsForCheckout(Set.of(10L))).thenReturn(List.of(
                new ProductSnapshot(10L, "Keyboard", money("25.00"), 5)
        ));
        when(saveOrderPort.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            return Order.restore(
                    100L, order.getCustomerId(), order.getItems(), order.getTotal(),
                    order.getStatus(), order.getCreatedAt(), order.getUpdatedAt()
            );
        });

        var service = new OrderService(
                cartFacade, catalogFacade, mock(LoadOrderPort.class), saveOrderPort,
                mock(OrderEventPublisherPort.class), Clock.fixed(now, ZoneOffset.UTC)
        );

        var result = service.checkout(7L);

        assertThat(result.id()).isEqualTo(100L);
        assertThat(result.total().amount()).isEqualByComparingTo("50.00");
        assertThat(result.status()).isEqualTo(OrderStatus.PENDING_PAYMENT);
        verify(catalogFacade).decreaseStock(any());
        verify(cartFacade).clear(7L);
    }

    private Money money(String amount) {
        return new Money(new BigDecimal(amount), "AZN");
    }
}
