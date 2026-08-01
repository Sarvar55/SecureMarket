package com.codems.securemarket.order.internal.adapter.in.decorator;

import com.codems.securemarket.order.internal.application.port.in.CheckoutOrderUseCase;
import com.codems.securemarket.order.internal.application.port.in.HandlePaymentResultUseCase;
import com.codems.securemarket.order.internal.application.port.in.QueryOrdersUseCase;
import com.codems.securemarket.order.internal.application.port.in.ManageOrderFulfillmentUseCase;
import com.codems.securemarket.order.internal.application.port.in.command.ChangeOrderStatusCommand;
import com.codems.securemarket.order.internal.application.port.in.query.OrderView;
import com.codems.securemarket.order.internal.application.service.OrderService;
import com.codems.securemarket.payment.api.event.PaymentStatus;
import java.time.Instant;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Component
@Transactional
class TransactionalOrderServiceDecorator
        implements CheckoutOrderUseCase, QueryOrdersUseCase,
        HandlePaymentResultUseCase, ManageOrderFulfillmentUseCase {

    private final OrderService delegate;

    TransactionalOrderServiceDecorator(OrderService delegate) {
        this.delegate = delegate;
    }

    @Override
    public OrderView checkout(Long customerId) {
        return delegate.checkout(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderView> getForCustomer(Long customerId) {
        return delegate.getForCustomer(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderView getById(Long customerId, Long orderId) {
        return delegate.getById(customerId, orderId);
    }

    @Override
    public void handle(
            Long orderId,
            Long customerId,
            PaymentStatus paymentStatus,
            Instant occurredAt
    ) {
        delegate.handle(orderId, customerId, paymentStatus, occurredAt);
    }

    @Override
    public OrderView changeStatus(ChangeOrderStatusCommand command) {
        return delegate.changeStatus(command);
    }
}
