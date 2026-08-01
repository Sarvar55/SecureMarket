package com.codems.securemarket.identity.internal.application.port.out;

public interface TokenVerifierPort {

    TokenClaims verify(String token);
}

