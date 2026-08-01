package com.codems.securemarket.order.api;

import java.math.BigDecimal;

public record TopProductSnapshot(
        Long productId,
        String productName,
        long unitsSold,
        BigDecimal revenue
) {
}
