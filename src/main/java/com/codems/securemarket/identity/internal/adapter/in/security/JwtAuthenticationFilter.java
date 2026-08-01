package com.codems.securemarket.identity.internal.adapter.in.security;

import com.codems.securemarket.identity.internal.application.port.out.LoadUserPort;
import com.codems.securemarket.identity.internal.application.port.out.TokenVerifierPort;
import com.codems.securemarket.identity.internal.domain.model.AccountStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenVerifierPort tokenVerifierPort;
    private final LoadUserPort loadUserPort;
    private AntPathMatcher matcher = new AntPathMatcher();
    private final List<String> publicPaths;

    JwtAuthenticationFilter(TokenVerifierPort tokenVerifierPort, LoadUserPort loadUserPort,
            @Qualifier("publicPaths") List<String> publicPaths) {
        this.tokenVerifierPort = tokenVerifierPort;
        this.loadUserPort = loadUserPort;
        this.publicPaths = publicPaths;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authorization.substring(BEARER_PREFIX.length());
            var claims = tokenVerifierPort.verify(token);
            var user = loadUserPort.findById(claims.userId()).orElse(null);

            if (user != null && user.getStatus() == AccountStatus.ACTIVE) {
                var principal = new AuthenticatedUser(
                        user.getId(),
                        user.getEmail().value(),
                        user.getRoles());

                var authorities = user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .toList();

                var authentication = new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (RuntimeException exception) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return publicPaths.stream().anyMatch(pattern -> matcher.match(pattern, path));
    }
}
