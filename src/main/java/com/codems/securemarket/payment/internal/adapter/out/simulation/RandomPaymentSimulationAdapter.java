package com.codems.securemarket.payment.internal.adapter.out.simulation;

import com.codems.securemarket.payment.internal.application.port.out.PaymentSimulationPort;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class RandomPaymentSimulationAdapter implements PaymentSimulationPort {

    private final double successRate;

    RandomPaymentSimulationAdapter(
            @Value("${payment.simulation.success-rate}") double successRate
    ) {
        if (successRate < 0 || successRate > 1) {
            throw new IllegalArgumentException("Payment success rate must be between 0 and 1");
        }
        this.successRate = successRate;
    }

    @Override
    public boolean isSuccessful(BigDecimal amount) {
        return ThreadLocalRandom.current().nextDouble() < successRate;
    }
}

