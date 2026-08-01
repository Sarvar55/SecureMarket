package com.codems.securemarket.catalog.internal.adapter.in.web.request;

import jakarta.validation.constraints.NotNull;

public record AdjustProductStockRequest(@NotNull Integer quantity) {
}
