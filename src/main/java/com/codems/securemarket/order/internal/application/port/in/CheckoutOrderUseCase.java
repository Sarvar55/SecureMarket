package com.codems.securemarket.order.internal.application.port.in;

import com.codems.securemarket.order.internal.application.port.in.query.OrderView;

public interface CheckoutOrderUseCase {
    OrderView checkout(Long customerId);
}
