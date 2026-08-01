package com.codems.securemarket.cart.internal.application.port.in.command;

public record ChangeCartItemCommand(Long customerId, Long productId, int quantity) {
}
