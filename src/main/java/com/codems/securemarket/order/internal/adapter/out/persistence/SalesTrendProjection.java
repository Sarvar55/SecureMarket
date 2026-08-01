package com.codems.securemarket.order.internal.adapter.out.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;

interface SalesTrendProjection {
    LocalDate getSaleDate();
    BigDecimal getRevenue();
    long getOrderCount();
}
