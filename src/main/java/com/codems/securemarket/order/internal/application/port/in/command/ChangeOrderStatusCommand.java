package com.codems.securemarket.order.internal.application.port.in.command;

import com.codems.securemarket.order.internal.domain.model.OrderStatus;

public record ChangeOrderStatusCommand(
        Long orderId,
        OrderStatus status,
        Long actorId
) {
}
