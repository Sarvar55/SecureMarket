package com.codems.securemarket.order.internal.adapter.out.persistence;

import java.math.BigDecimal;

interface OrderAnalyticsSummaryProjection {
    BigDecimal getCurrentRevenue();
    long getCurrentOrderCount();
    long getPendingOrderCount();
    long getFailedOrderCount();
    long getDeliveredOrderCount();
    BigDecimal getPreviousRevenue();
    long getPreviousOrderCount();
}
