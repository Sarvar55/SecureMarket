package com.codems.securemarket.shared.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void normalizesScaleAndCurrency() {
        Money money = new Money(new BigDecimal("10.126"), "azn");

        assertThat(money.amount()).isEqualByComparingTo("10.13");
        assertThat(money.currency()).isEqualTo("AZN");
    }

    @Test
    void rejectsNegativeAmount() {
        assertThatThrownBy(() -> new Money(new BigDecimal("-0.01"), "AZN"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
