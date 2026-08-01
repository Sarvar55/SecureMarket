package com.codems.securemarket.cart.internal.adapter.in.decorator;

import com.codems.securemarket.cart.internal.application.port.in.ManageCartUseCase;
import com.codems.securemarket.cart.internal.application.port.in.command.AddCartItemCommand;
import com.codems.securemarket.cart.internal.application.port.in.command.ChangeCartItemCommand;
import com.codems.securemarket.cart.internal.application.port.in.query.CartView;
import com.codems.securemarket.cart.internal.application.service.CartService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Component
@Transactional
class TransactionalCartServiceDecorator implements ManageCartUseCase {

    private final CartService delegate;

    TransactionalCartServiceDecorator(CartService delegate) {
        this.delegate = delegate;
    }

    @Override
    @Transactional(readOnly = true)
    public CartView get(Long customerId) {
        return delegate.get(customerId);
    }

    @Override
    public CartView add(AddCartItemCommand command) {
        return delegate.add(command);
    }

    @Override
    public CartView change(ChangeCartItemCommand command) {
        return delegate.change(command);
    }

    @Override
    public CartView remove(Long customerId, Long productId) {
        return delegate.remove(customerId, productId);
    }

    @Override
    public void clear(Long customerId) {
        delegate.clear(customerId);
    }
}
