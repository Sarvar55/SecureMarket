package com.codems.securemarket.identity.internal.application.port.out;

import java.time.Instant;

public record IssuedToken(String value, Instant expiresAt) {
}

