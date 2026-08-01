package com.codems.securemarket.identity.internal.adapter.out.token;

import com.codems.securemarket.identity.internal.application.port.out.IssuedToken;
import com.codems.securemarket.identity.internal.application.port.out.TokenClaims;
import com.codems.securemarket.identity.internal.application.port.out.TokenIssuerPort;
import com.codems.securemarket.identity.internal.application.port.out.TokenVerifierPort;
import com.codems.securemarket.identity.internal.config.JwtProperties;
import com.codems.securemarket.identity.internal.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
class JwtTokenAdapter implements TokenIssuerPort, TokenVerifierPort {

    private final JwtProperties properties;
    private final Clock clock;
    private final SecretKey signingKey;

    JwtTokenAdapter(JwtProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.secret()));
    }

    @Override
    public IssuedToken issue(User user) {
        Instant issuedAt = clock.instant();
        Instant expiresAt = issuedAt.plus(properties.accessTokenTtl());
        List<String> roles = user.getRoles().stream().map(Enum::name).sorted().toList();

        String token = Jwts.builder()
                .issuer(properties.issuer())
                .subject(user.getId().toString())
                .claim("roles", roles)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();

        return new IssuedToken(token, expiresAt);
    }

    @Override
    public TokenClaims verify(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(properties.issuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        List<?> rawRoles = claims.get("roles", List.class);
        Set<String> roles = rawRoles == null
                ? Set.of()
                : rawRoles.stream().map(String::valueOf).collect(Collectors.toUnmodifiableSet());

        return new TokenClaims(
                Long.valueOf(claims.getSubject()),
                roles,
                claims.getExpiration().toInstant()
        );
    }
}

