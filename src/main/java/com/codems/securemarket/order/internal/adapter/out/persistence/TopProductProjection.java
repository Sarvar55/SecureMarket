package com.codems.securemarket.order.internal.adapter.out.persistence;

import java.math.BigDecimal;

interface TopProductProjection {
    Long getProductId();
    String getProductName();
    long getUnitsSold();
    BigDecimal getRevenue();
}
