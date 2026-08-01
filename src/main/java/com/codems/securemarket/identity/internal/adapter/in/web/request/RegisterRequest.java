package com.codems.securemarket.identity.internal.adapter.in.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
                @NotBlank @Email String email,
                @NotBlank @Size(min = 12, max = 72) String password) {
}
