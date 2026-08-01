package com.codems.securemarket.order.internal.application.port.out;

import com.codems.securemarket.order.internal.domain.model.Order;

public interface SaveOrderPort {
    Order save(Order order);
}
