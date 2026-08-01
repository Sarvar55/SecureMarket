package com.codems.securemarket.order.api;

import java.math.BigDecimal;
import java.util.List;

public record OrderAnalyticsSnapshot(
        BigDecimal currentRevenue,
        long currentOrderCount,
        long pendingOrderCount,
        long failedOrderCount,
        long deliveredOrderCount,
        BigDecimal previousRevenue,
        long previousOrderCount,
        List<SalesTrendSnapshot> salesTrend,
        List<TopProductSnapshot> topProducts
) {
}
