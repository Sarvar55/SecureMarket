package com.codems.securemarket.cart.internal.adapter.out.persistence;

import com.codems.securemarket.cart.internal.application.port.out.LoadCartPort;
import com.codems.securemarket.cart.internal.application.port.out.SaveCartPort;
import com.codems.securemarket.cart.internal.domain.model.Cart;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class CartPersistenceAdapter implements LoadCartPort, SaveCartPort {
    private final JpaCartRepository repository;

    CartPersistenceAdapter(JpaCartRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Cart> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId).map(CartEntity::toDomain);
    }

    @Override
    public Cart save(Cart cart) {
        CartEntity entity = cart.getId() == null
                ? CartEntity.from(cart)
                : repository.findById(cart.getId()).orElseGet(() -> CartEntity.from(cart));
        entity.updateFrom(cart);
        return repository.save(entity).toDomain();
    }
}
