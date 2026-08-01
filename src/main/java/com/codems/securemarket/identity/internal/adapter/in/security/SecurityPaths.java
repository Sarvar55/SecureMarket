package com.codems.securemarket.identity.internal.adapter.in.security;

import static com.codems.securemarket.shared.constants.ApplicationConstants.API_PREFIX;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SecurityPaths {

    @Bean("publicPaths")
    List<String> publicPaths() {
        return List.of(
                API_PREFIX + "/auth/**",
                API_PREFIX + "/categories/**",
                API_PREFIX + "/products/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/actuator/health");
    }

    @Bean("securedPaths")
    List<String> securedPaths() {
        return List.of(
                API_PREFIX + "/cart/**",
                API_PREFIX + "/orders/**",
                API_PREFIX + "/notifications/**");
    }

    @Bean("catalogAdminPaths")
    List<String> catalogAdminPaths() {
        return List.of(API_PREFIX + "/admin/catalog/**");
    }

    @Bean("orderAdminPaths")
    List<String> orderAdminPaths() {
        return List.of(API_PREFIX + "/admin/orders/**");
    }

    @Bean("userAdminPaths")
    List<String> userAdminPaths() {
        return List.of(API_PREFIX + "/admin/users/**");
    }

    @Bean("auditAdminPaths")
    List<String> auditAdminPaths() {
        return List.of(API_PREFIX + "/admin/audit-events/**");
    }

    @Bean("analyticsAdminPaths")
    List<String> analyticsAdminPaths() {
        return List.of(API_PREFIX + "/admin/analytics/**");
    }
}
