package com.codems.securemarket.cart.internal.application.port.in.command;

public record AddCartItemCommand(Long customerId, Long productId, int quantity) {
}
