package com.codems.securemarket.identity.internal.application.port.out;

import java.time.Instant;
import java.util.Set;

public record TokenClaims(
        Long userId,
        Set<String> roles,
        Instant expiresAt
) {
}

