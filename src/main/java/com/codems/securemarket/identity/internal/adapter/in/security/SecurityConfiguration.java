package com.codems.securemarket.identity.internal.adapter.in.security;

import com.codems.securemarket.shared.security.RestAccessDeniedHandler;
import com.codems.securemarket.shared.security.RestAuthenticationEntryPoint;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
class SecurityConfiguration {

        @Bean
        SecurityFilterChain securityFilterChain(
                        HttpSecurity http,
                        JwtAuthenticationFilter jwtAuthenticationFilter,
                        RestAuthenticationEntryPoint authenticationEntryPoint,
                        RestAccessDeniedHandler accessDeniedHandler,
                        @Qualifier("publicPaths") List<String> publicPaths,
                        @Qualifier("securedPaths") List<String> securedPaths,
                        @Qualifier("catalogAdminPaths") List<String> catalogAdminPaths,
                        @Qualifier("orderAdminPaths") List<String> orderAdminPaths,
                        @Qualifier("userAdminPaths") List<String> userAdminPaths,
                        @Qualifier("auditAdminPaths") List<String> auditAdminPaths,
                        @Qualifier("analyticsAdminPaths") List<String> analyticsAdminPaths
        ) throws Exception {
                return http
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(authenticationEntryPoint)
                                                .accessDeniedHandler(accessDeniedHandler))
                                .authorizeHttpRequests(authorize -> {
                                        publicPaths.forEach(path -> authorize
                                                        .requestMatchers(path).permitAll());
                                        catalogAdminPaths.forEach(path -> authorize
                                                        .requestMatchers(path)
                                                        .hasAnyRole("CATALOG_ADMIN", "SUPER_ADMIN"));
                                        orderAdminPaths.forEach(path -> authorize
                                                        .requestMatchers(path)
                                                        .hasAnyRole("ORDER_ADMIN", "SUPER_ADMIN"));
                                        userAdminPaths.forEach(path -> authorize
                                                        .requestMatchers(path)
                                                        .hasAnyRole("USER_ADMIN", "SUPER_ADMIN"));
                                        auditAdminPaths.forEach(path -> authorize
                                                        .requestMatchers(path)
                                                        .hasAnyRole("AUDIT_ADMIN", "SUPER_ADMIN"));
                                        analyticsAdminPaths.forEach(path -> authorize
                                                        .requestMatchers(path)
                                                        .hasAnyRole("ANALYTICS_ADMIN", "SUPER_ADMIN"));
                                        securedPaths.forEach(path -> authorize
                                                        .requestMatchers(path).authenticated());
                                        authorize.anyRequest().denyAll();
                                })
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .build();
        }
}
