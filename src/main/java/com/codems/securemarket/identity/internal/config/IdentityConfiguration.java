package com.codems.securemarket.identity.internal.config;

import com.codems.securemarket.identity.internal.application.port.in.LoginUseCase;
import com.codems.securemarket.identity.internal.application.port.in.ManageUsersUseCase;
import com.codems.securemarket.identity.internal.application.port.in.RegisterUserUseCase;
import com.codems.securemarket.identity.internal.application.port.out.AuthenticateCredentialsPort;
import com.codems.securemarket.identity.internal.application.port.out.IdentityEventPublisherPort;
import com.codems.securemarket.identity.internal.application.port.out.LoadUserPort;
import com.codems.securemarket.identity.internal.application.port.out.PasswordHasherPort;
import com.codems.securemarket.identity.internal.application.port.out.SaveUserPort;
import com.codems.securemarket.identity.internal.application.port.out.TokenIssuerPort;
import com.codems.securemarket.identity.internal.application.service.AuthenticationService;
import com.codems.securemarket.identity.internal.application.service.RegistrationService;
import com.codems.securemarket.identity.internal.application.service.UserManagementService;
import java.time.Clock;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class IdentityConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider,
            ApplicationEventPublisher applicationEventPublisher) {
        ProviderManager authenticationManager = new ProviderManager(authenticationProvider);
        authenticationManager
                .setAuthenticationEventPublisher(new DefaultAuthenticationEventPublisher(applicationEventPublisher));
        return authenticationManager;
    }

    @Bean
    RegisterUserUseCase registerUserUseCase(
            LoadUserPort loadUserPort,
            SaveUserPort saveUserPort,
            PasswordHasherPort passwordHasherPort,
            IdentityEventPublisherPort eventPublisher,
            Clock clock) {
        return new RegistrationService(
                loadUserPort,
                saveUserPort,
                passwordHasherPort,
                eventPublisher,
                clock);
    }

    @Bean
    LoginUseCase loginUseCase(
            AuthenticateCredentialsPort authenticateCredentialsPort,
            SaveUserPort saveUserPort,
            TokenIssuerPort tokenIssuerPort,
            IdentityEventPublisherPort eventPublisher,
            Clock clock) {
        return new AuthenticationService(
                authenticateCredentialsPort,
                saveUserPort,
                tokenIssuerPort,
                eventPublisher,
                clock);
    }

    @Bean
    ManageUsersUseCase manageUsersUseCase(
            LoadUserPort loadUserPort,
            SaveUserPort saveUserPort,
            IdentityEventPublisherPort eventPublisher,
            Clock clock) {
        return new UserManagementService(
                loadUserPort,
                saveUserPort,
                eventPublisher,
                clock);
    }
}
