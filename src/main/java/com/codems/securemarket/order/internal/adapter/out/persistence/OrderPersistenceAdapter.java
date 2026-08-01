package com.codems.securemarket.order.internal.adapter.out.persistence;

import com.codems.securemarket.order.internal.application.port.out.LoadOrderPort;
import com.codems.securemarket.order.internal.application.port.out.SaveOrderPort;
import com.codems.securemarket.order.internal.domain.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class OrderPersistenceAdapter implements LoadOrderPort, SaveOrderPort {
    private final JpaOrderRepository repository;

    OrderPersistenceAdapter(JpaOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return repository.findById(orderId).map(OrderEntity::toDomain);
    }

    @Override
    public Optional<Order> findByIdAndCustomerId(Long orderId, Long customerId) {
        return repository.findByIdAndCustomerId(orderId, customerId).map(OrderEntity::toDomain);
    }

    @Override
    public List<Order> findAllByCustomerId(Long customerId) {
        return repository.findAllByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(OrderEntity::toDomain)
                .toList();
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = order.getId() == null
                ? OrderEntity.from(order)
                : repository.findById(order.getId()).orElseGet(() -> OrderEntity.from(order));
        entity.updateFrom(order);
        return repository.save(entity).toDomain();
    }
}
