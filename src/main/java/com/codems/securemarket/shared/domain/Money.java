package com.codems.securemarket.shared.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Objects;

public record Money(BigDecimal amount, String currency) implements Serializable {

    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency must not be blank");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
        currency = currency.trim().toUpperCase(Locale.ROOT);

        if (currency.length() != 3) {
            throw new IllegalArgumentException("Currency must contain three letters");
        }
    }

    public boolean isPositive() {
        return amount.signum() > 0;
    }

    public Money multiply(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        return new Money(amount.multiply(BigDecimal.valueOf(quantity)), currency);
    }
}

