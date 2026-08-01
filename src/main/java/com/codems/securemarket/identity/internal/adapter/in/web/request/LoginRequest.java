package com.codems.securemarket.identity.internal.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
                @NotBlank String email,
                @NotBlank String password) {
}
