package com.codems.securemarket.payment.internal.config;

import com.codems.securemarket.payment.internal.application.port.in.ProcessPaymentUseCase;
import com.codems.securemarket.payment.internal.application.port.out.PaymentEventPublisherPort;
import com.codems.securemarket.payment.internal.application.port.out.PaymentSimulationPort;
import com.codems.securemarket.payment.internal.application.service.PaymentService;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfiguration {

    @Bean
    ProcessPaymentUseCase processPaymentUseCase(
            PaymentSimulationPort simulationPort,
            PaymentEventPublisherPort eventPublisher,
            Clock clock
    ) {
        return new PaymentService(simulationPort, eventPublisher, clock);
    }
}

