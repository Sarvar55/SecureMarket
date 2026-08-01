package com.codems.securemarket.payment.internal.application.port.out;

import java.math.BigDecimal;

public interface PaymentSimulationPort {

    boolean isSuccessful(BigDecimal amount);
}

