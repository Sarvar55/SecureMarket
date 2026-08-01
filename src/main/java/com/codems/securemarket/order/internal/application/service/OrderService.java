package com.codems.securemarket.order.internal.application.service;

import com.codems.securemarket.cart.api.CartFacade;
import com.codems.securemarket.catalog.api.CatalogFacade;
import com.codems.securemarket.catalog.api.ProductSnapshot;
import com.codems.securemarket.catalog.api.StockRequest;
import com.codems.securemarket.order.api.event.OrderCreatedEvent;
import com.codems.securemarket.order.api.event.CheckoutStartedEvent;
import com.codems.securemarket.order.api.event.OrderStatusChangedEvent;
import com.codems.securemarket.order.api.event.PaymentRequestedEvent;
import com.codems.securemarket.order.internal.application.port.in.CheckoutOrderUseCase;
import com.codems.securemarket.order.internal.application.port.in.HandlePaymentResultUseCase;
import com.codems.securemarket.order.internal.application.port.in.ManageOrderFulfillmentUseCase;
import com.codems.securemarket.order.internal.application.port.in.command.ChangeOrderStatusCommand;
import com.codems.securemarket.order.internal.application.port.in.query.OrderView;
import com.codems.securemarket.order.internal.application.port.in.QueryOrdersUseCase;
import com.codems.securemarket.order.internal.application.port.out.LoadOrderPort;
import com.codems.securemarket.order.internal.application.port.out.OrderEventPublisherPort;
import com.codems.securemarket.order.internal.application.port.out.SaveOrderPort;
import com.codems.securemarket.order.internal.domain.exception.EmptyCartException;
import com.codems.securemarket.order.internal.domain.exception.OrderNotFoundException;
import com.codems.securemarket.order.internal.domain.model.Order;
import com.codems.securemarket.order.internal.domain.model.OrderItem;
import com.codems.securemarket.payment.api.event.PaymentStatus;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class OrderService implements CheckoutOrderUseCase, QueryOrdersUseCase,
        HandlePaymentResultUseCase, ManageOrderFulfillmentUseCase {

    private final CartFacade cartFacade;
    private final CatalogFacade catalogFacade;
    private final LoadOrderPort loadOrderPort;
    private final SaveOrderPort saveOrderPort;
    private final OrderEventPublisherPort eventPublisher;
    private final Clock clock;

    public OrderService(
            CartFacade cartFacade,
            CatalogFacade catalogFacade,
            LoadOrderPort loadOrderPort,
            SaveOrderPort saveOrderPort,
            OrderEventPublisherPort eventPublisher,
            Clock clock
    ) {
        this.cartFacade = cartFacade;
        this.catalogFacade = catalogFacade;
        this.loadOrderPort = loadOrderPort;
        this.saveOrderPort = saveOrderPort;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    public OrderView checkout(Long customerId) {
        var checkoutStartedAt = clock.instant();
        eventPublisher.publish(new CheckoutStartedEvent(
                UUID.randomUUID(), customerId, checkoutStartedAt
        ));
        var cart = cartFacade.getCart(customerId);
        if (cart.items().isEmpty()) throw new EmptyCartException();

        Set<Long> productIds = cart.items().stream()
                .map(item -> item.productId())
                .collect(Collectors.toSet());
        Map<Long, ProductSnapshot> products = catalogFacade.getProductsForCheckout(productIds)
                .stream()
                .collect(Collectors.toMap(ProductSnapshot::productId, Function.identity()));

        List<OrderItem> items = cart.items().stream()
                .map(item -> {
                    ProductSnapshot product = products.get(item.productId());
                    return new OrderItem(
                            product.productId(), product.name(), product.unitPrice(), item.quantity()
                    );
                })
                .toList();

        var now = clock.instant();
        Order saved = saveOrderPort.save(Order.create(customerId, items, now));
        List<StockRequest> stockRequests = cart.items().stream()
                .map(item -> new StockRequest(item.productId(), item.quantity()))
                .toList();
        catalogFacade.decreaseStock(stockRequests);
        cartFacade.clear(customerId);

        eventPublisher.publish(new OrderCreatedEvent(
                UUID.randomUUID(), saved.getId(), customerId, now
        ));
        eventPublisher.publish(new PaymentRequestedEvent(
                UUID.randomUUID(), saved.getId(), customerId,
                saved.getTotal().amount(), saved.getTotal().currency(), now
        ));
        return OrderView.from(saved);
    }

    @Override
    public List<OrderView> getForCustomer(Long customerId) {
        return loadOrderPort.findAllByCustomerId(customerId).stream()
                .map(OrderView::from)
                .toList();
    }

    @Override
    public OrderView getById(Long customerId, Long orderId) {
        return loadOrderPort.findByIdAndCustomerId(orderId, customerId)
                .map(OrderView::from)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public void handle(Long orderId, Long customerId, PaymentStatus paymentStatus, Instant occurredAt) {
        Order order = loadOrderPort.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        if (!order.getCustomerId().equals(customerId) || !order.isPaymentPending()) return;

        var previousStatus = order.getStatus();
        if (paymentStatus == PaymentStatus.SUCCEEDED) {
            order.markPaid(occurredAt);
        } else {
            order.markPaymentFailed(occurredAt);
            catalogFacade.increaseStock(order.getItems().stream()
                    .map(item -> new StockRequest(item.productId(), item.quantity()))
                    .toList());
        }
        Order saved = saveOrderPort.save(order);
        eventPublisher.publish(new OrderStatusChangedEvent(
                UUID.randomUUID(), saved.getId(), saved.getCustomerId(),
                previousStatus.name(), saved.getStatus().name(), null, occurredAt
        ));
    }

    @Override
    public OrderView changeStatus(ChangeOrderStatusCommand command) {
        Order order = loadOrderPort.findById(command.orderId())
                .orElseThrow(OrderNotFoundException::new);
        var previousStatus = order.getStatus();
        var now = clock.instant();
        order.advanceFulfillment(command.status(), now);
        Order saved = saveOrderPort.save(order);
        eventPublisher.publish(new OrderStatusChangedEvent(
                UUID.randomUUID(), saved.getId(), saved.getCustomerId(),
                previousStatus.name(), saved.getStatus().name(), command.actorId(), now
        ));
        return OrderView.from(saved);
    }
}
