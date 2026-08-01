package com.codems.securemarket.cart.internal.adapter.in.facade;

import com.codems.securemarket.cart.api.CartFacade;
import com.codems.securemarket.cart.api.CartLineSnapshot;
import com.codems.securemarket.cart.api.CartSnapshot;
import com.codems.securemarket.cart.internal.application.port.in.ManageCartUseCase;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class CartFacadeAdapter implements CartFacade {
    private final ManageCartUseCase manageCartUseCase;

    CartFacadeAdapter(ManageCartUseCase manageCartUseCase) {
        this.manageCartUseCase = manageCartUseCase;
    }

    @Override
    @Transactional(readOnly = true)
    public CartSnapshot getCart(Long customerId) {
        var view = manageCartUseCase.get(customerId);
        return new CartSnapshot(
                view.customerId(),
                view.items().stream()
                        .map(item -> new CartLineSnapshot(item.productId(), item.quantity()))
                        .toList()
        );
    }

    @Override
    @Transactional
    public void clear(Long customerId) {
        manageCartUseCase.clear(customerId);
    }
}
