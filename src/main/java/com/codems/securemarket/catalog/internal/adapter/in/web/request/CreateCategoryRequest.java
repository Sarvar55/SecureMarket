package com.codems.securemarket.catalog.internal.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 120) String slug
) {
}
