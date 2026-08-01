package com.codems.securemarket.identity.internal.application.port.in.query;

import java.time.Instant;

public record AccessTokenView(
        String accessToken,
        String tokenType,
        Instant expiresAt
) {
    public AccessTokenView(String accessToken, Instant expiresAt) {
        this(accessToken, "Bearer", expiresAt);
    }
}

