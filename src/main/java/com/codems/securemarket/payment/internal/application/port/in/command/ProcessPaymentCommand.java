package com.codems.securemarket.payment.internal.application.port.in.command;

import java.math.BigDecimal;
import java.util.Objects;

public record ProcessPaymentCommand(
        Long orderId,
        Long customerId,
        BigDecimal amount,
        String currency
) {
    public ProcessPaymentCommand {
        Objects.requireNonNull(orderId, "orderId must not be null");
        Objects.requireNonNull(customerId, "customerId must not be null");
        Objects.requireNonNull(amount, "amount must not be null");

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency must not be blank");
        }

        currency = currency.trim().toUpperCase();
    }
}

