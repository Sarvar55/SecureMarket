package com.codems.securemarket.order.internal.adapter.in.web.request;

import com.codems.securemarket.order.internal.domain.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeOrderStatusRequest(@NotNull OrderStatus status) {
}
