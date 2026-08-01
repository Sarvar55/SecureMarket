package com.codems.securemarket.cart.internal.adapter.in.web.request;

import jakarta.validation.constraints.Min;

public record CartQuantityRequest(@Min(1) int quantity) {
}
