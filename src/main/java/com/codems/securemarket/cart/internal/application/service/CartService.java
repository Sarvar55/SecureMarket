package com.codems.securemarket.cart.internal.application.service;

import com.codems.securemarket.cart.api.event.CartClearedEvent;
import com.codems.securemarket.cart.api.event.CartItemChangedEvent;
import com.codems.securemarket.cart.internal.application.port.in.query.CartView;
import com.codems.securemarket.cart.internal.application.port.in.ManageCartUseCase;
import com.codems.securemarket.cart.internal.application.port.in.command.AddCartItemCommand;
import com.codems.securemarket.cart.internal.application.port.in.command.ChangeCartItemCommand;
import com.codems.securemarket.cart.internal.application.port.out.CartEventPublisherPort;
import com.codems.securemarket.cart.internal.application.port.out.CheckProductQuantityPort;
import com.codems.securemarket.cart.internal.application.port.out.LoadCartPort;
import com.codems.securemarket.cart.internal.application.port.out.SaveCartPort;
import com.codems.securemarket.cart.internal.domain.exception.InvalidCartQuantityException;
import com.codems.securemarket.cart.internal.domain.model.Cart;
import java.time.Clock;
import java.util.UUID;

public final class CartService implements ManageCartUseCase {

    private final LoadCartPort loadCartPort;
    private final SaveCartPort saveCartPort;
    private final CheckProductQuantityPort checkProductQuantityPort;
    private final CartEventPublisherPort eventPublisher;
    private final Clock clock;

    public CartService(
            LoadCartPort loadCartPort,
            SaveCartPort saveCartPort,
            CheckProductQuantityPort checkProductQuantityPort,
            CartEventPublisherPort eventPublisher,
            Clock clock
    ) {
        this.loadCartPort = loadCartPort;
        this.saveCartPort = saveCartPort;
        this.checkProductQuantityPort = checkProductQuantityPort;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    public CartView get(Long customerId) {
        return CartView.from(loadOrNew(customerId));
    }

    @Override
    public CartView add(AddCartItemCommand command) {
        if (command.quantity() <= 0) {
            throw new InvalidCartQuantityException();
        }
        Cart cart = loadOrNew(command.customerId());
        int newQuantity = cart.quantityOf(command.productId()) + command.quantity();
        checkProductQuantityPort.check(command.productId(), newQuantity);
        return saveChanged(cart, command.productId(), newQuantity);
    }

    @Override
    public CartView change(ChangeCartItemCommand command) {
        checkProductQuantityPort.check(command.productId(), command.quantity());
        return saveChanged(loadOrNew(command.customerId()), command.productId(), command.quantity());
    }

    @Override
    public CartView remove(Long customerId, Long productId) {
        Cart cart = loadOrNew(customerId);
        var now = clock.instant();
        cart.remove(productId, now);
        Cart saved = saveCartPort.save(cart);
        eventPublisher.publish(new CartItemChangedEvent(
                UUID.randomUUID(), customerId, productId, 0, now
        ));
        return CartView.from(saved);
    }

    @Override
    public void clear(Long customerId) {
        loadCartPort.findByCustomerId(customerId).ifPresent(cart -> {
            var now = clock.instant();
            cart.clear(now);
            saveCartPort.save(cart);
            eventPublisher.publish(new CartClearedEvent(UUID.randomUUID(), customerId, now));
        });
    }

    private CartView saveChanged(Cart cart, Long productId, int quantity) {
        var now = clock.instant();
        cart.put(productId, quantity, now);
        Cart saved = saveCartPort.save(cart);
        eventPublisher.publish(new CartItemChangedEvent(
                UUID.randomUUID(), saved.getCustomerId(), productId, quantity, now
        ));
        return CartView.from(saved);
    }

    private Cart loadOrNew(Long customerId) {
        return loadCartPort.findByCustomerId(customerId)
                .orElseGet(() -> Cart.create(customerId, clock.instant()));
    }
}
