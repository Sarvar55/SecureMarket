package com.codems.securemarket.catalog.internal.adapter.in.web.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CreateProductRequest(
        @NotNull Long categoryId,
        @NotBlank @Size(max = 64) String sku,
        @NotBlank @Size(max = 180) String name,
        @Size(max = 1000) String description,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @Min(0) int stock
) {
}
