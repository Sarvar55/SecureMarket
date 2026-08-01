package com.codems.securemarket.cart.internal.config;

import com.codems.securemarket.cart.internal.application.port.out.CartEventPublisherPort;
import com.codems.securemarket.cart.internal.application.port.out.CheckProductQuantityPort;
import com.codems.securemarket.cart.internal.application.port.out.LoadCartPort;
import com.codems.securemarket.cart.internal.application.port.out.SaveCartPort;
import com.codems.securemarket.cart.internal.application.service.CartService;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartConfiguration {
    @Bean
    CartService cartService(
            LoadCartPort loadCartPort,
            SaveCartPort saveCartPort,
            CheckProductQuantityPort checkProductQuantityPort,
            CartEventPublisherPort eventPublisher,
            Clock clock
    ) {
        return new CartService(
                loadCartPort, saveCartPort, checkProductQuantityPort, eventPublisher, clock
        );
    }
}
