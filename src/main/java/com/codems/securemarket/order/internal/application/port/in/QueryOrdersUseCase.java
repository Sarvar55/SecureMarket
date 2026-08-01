package com.codems.securemarket.order.internal.application.port.in;

import com.codems.securemarket.order.internal.application.port.in.query.OrderView;

import java.util.List;

public interface QueryOrdersUseCase {
    List<OrderView> getForCustomer(Long customerId);
    OrderView getById(Long customerId, Long orderId);
}
