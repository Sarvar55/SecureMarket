package com.codems.securemarket.catalog.internal.adapter.in.web.request;

import com.codems.securemarket.catalog.internal.domain.model.ProductStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeProductStatusRequest(@NotNull ProductStatus status) {
}
