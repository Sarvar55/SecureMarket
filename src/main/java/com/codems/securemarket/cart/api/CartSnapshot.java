package com.codems.securemarket.cart.api;

import java.util.List;

public record CartSnapshot(Long customerId, List<CartLineSnapshot> items) {
}
