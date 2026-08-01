package com.codems.securemarket.order.internal.application.port.in.query;

import com.codems.securemarket.order.internal.domain.model.Order;
import com.codems.securemarket.order.internal.domain.model.OrderItem;
import com.codems.securemarket.order.internal.domain.model.OrderStatus;
import com.codems.securemarket.shared.domain.Money;
import java.time.Instant;
import java.util.List;

public record OrderView(
        Long id,
        List<OrderItem> items,
        Money total,
        OrderStatus status,
        Instant createdAt
) {
    public static OrderView from(Order order) {
        return new OrderView(
                order.getId(), order.getItems(), order.getTotal(),
                order.getStatus(), order.getCreatedAt()
        );
    }
}
