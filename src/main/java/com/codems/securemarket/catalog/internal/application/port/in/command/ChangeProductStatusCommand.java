package com.codems.securemarket.catalog.internal.application.port.in.command;

import com.codems.securemarket.catalog.internal.domain.model.ProductStatus;

public record ChangeProductStatusCommand(Long productId, ProductStatus status, Long actorId) {
}
