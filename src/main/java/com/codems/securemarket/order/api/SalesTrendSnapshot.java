package com.codems.securemarket.order.api;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalesTrendSnapshot(
        LocalDate date,
        BigDecimal revenue,
        long orderCount
) {
}
