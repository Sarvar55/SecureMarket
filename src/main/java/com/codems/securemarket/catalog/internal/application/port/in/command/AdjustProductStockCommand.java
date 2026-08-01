package com.codems.securemarket.catalog.internal.application.port.in.command;

public record AdjustProductStockCommand(Long productId, int quantity, Long actorId) {
}
