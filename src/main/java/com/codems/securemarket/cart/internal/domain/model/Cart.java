package com.codems.securemarket.cart.internal.domain.model;

import com.codems.securemarket.cart.internal.domain.exception.CartItemNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Cart {

    private final Long id;
    private final Long customerId;
    private final Map<Long, CartItem> items;
    private final Instant createdAt;
    private Instant updatedAt;

    private Cart(Long id, Long customerId, List<CartItem> items, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.customerId = Objects.requireNonNull(customerId);
        this.items = new LinkedHashMap<>();
        items.forEach(item -> this.items.put(item.productId(), item));
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static Cart create(Long customerId, Instant now) {
        return new Cart(null, customerId, List.of(), now, now);
    }

    public static Cart restore(Long id, Long customerId, List<CartItem> items, Instant createdAt, Instant updatedAt) {
        return new Cart(Objects.requireNonNull(id), customerId, items, createdAt, updatedAt);
    }

    public int quantityOf(Long productId) {
        CartItem item = items.get(productId);
        return item == null ? 0 : item.quantity();
    }

    public void put(Long productId, int quantity, Instant now) {
        items.put(productId, new CartItem(productId, quantity));
        updatedAt = Objects.requireNonNull(now);
    }

    public void remove(Long productId, Instant now) {
        if (items.remove(productId) == null) {
            throw new CartItemNotFoundException();
        }
        updatedAt = Objects.requireNonNull(now);
    }

    public void clear(Instant now) {
        items.clear();
        updatedAt = Objects.requireNonNull(now);
    }

    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public List<CartItem> getItems() { return new ArrayList<>(items.values()); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
