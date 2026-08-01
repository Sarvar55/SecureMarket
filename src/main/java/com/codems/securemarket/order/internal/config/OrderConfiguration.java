package com.codems.securemarket.order.internal.config;

import com.codems.securemarket.cart.api.CartFacade;
import com.codems.securemarket.catalog.api.CatalogFacade;
import com.codems.securemarket.order.internal.application.port.out.LoadOrderPort;
import com.codems.securemarket.order.internal.application.port.out.OrderEventPublisherPort;
import com.codems.securemarket.order.internal.application.port.out.SaveOrderPort;
import com.codems.securemarket.order.internal.application.service.OrderService;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfiguration {
    @Bean
    OrderService orderService(
            CartFacade cartFacade,
            CatalogFacade catalogFacade,
            LoadOrderPort loadOrderPort,
            SaveOrderPort saveOrderPort,
            OrderEventPublisherPort eventPublisher,
            Clock clock
    ) {
        return new OrderService(
                cartFacade, catalogFacade, loadOrderPort, saveOrderPort, eventPublisher, clock
        );
    }
}
