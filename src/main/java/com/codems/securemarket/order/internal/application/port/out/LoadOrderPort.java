package com.codems.securemarket.order.internal.application.port.out;

import com.codems.securemarket.order.internal.domain.model.Order;
import java.util.List;
import java.util.Optional;

public interface LoadOrderPort {
    Optional<Order> findById(Long orderId);
    Optional<Order> findByIdAndCustomerId(Long orderId, Long customerId);
    List<Order> findAllByCustomerId(Long customerId);
}
