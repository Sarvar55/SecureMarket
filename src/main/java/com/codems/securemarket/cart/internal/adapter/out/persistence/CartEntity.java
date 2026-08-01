package com.codems.securemarket.cart.internal.adapter.out.persistence;

import com.codems.securemarket.cart.internal.domain.model.Cart;
import com.codems.securemarket.cart.internal.domain.model.CartItem;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "carts")
class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false, unique = true)
    private Long customerId;

    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity", nullable = false)
    private Map<Long, Integer> items = new LinkedHashMap<>();

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private long version;

    protected CartEntity() {
    }

    static CartEntity from(Cart cart) {
        var entity = new CartEntity();
        entity.id = cart.getId();
        entity.updateFrom(cart);
        return entity;
    }

    void updateFrom(Cart cart) {
        customerId = cart.getCustomerId();
        items.clear();
        cart.getItems().forEach(item -> items.put(item.productId(), item.quantity()));
        createdAt = cart.getCreatedAt();
        updatedAt = cart.getUpdatedAt();
    }

    Cart toDomain() {
        return Cart.restore(
                id, customerId,
                items.entrySet().stream()
                        .map(entry -> new CartItem(entry.getKey(), entry.getValue()))
                        .toList(),
                createdAt, updatedAt
        );
    }
}
