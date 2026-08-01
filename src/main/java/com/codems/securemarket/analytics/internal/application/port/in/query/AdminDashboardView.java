package com.codems.securemarket.analytics.internal.application.port.in.query;

import com.codems.securemarket.shared.domain.Money;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record AdminDashboardView(
        Instant periodStart,
        Instant periodEnd,
        Instant generatedAt,
        String currency,
        SalesSummaryView summary,
        SalesVelocityView velocity,
        MetricChangeView changes,
        List<SalesTrendView> salesTrend,
        List<TopProductView> topProducts
) {
    public record SalesSummaryView(
            Money grossRevenue,
            long successfulOrders,
            long pendingOrders,
            long failedOrders,
            long deliveredOrders,
            Money averageOrderValue
    ) {
    }

    public record SalesVelocityView(
            Money revenuePerDay,
            BigDecimal ordersPerDay
    ) {
    }

    public record MetricChangeView(
            BigDecimal revenueChangePercentage,
            BigDecimal orderCountChangePercentage
    ) {
    }

    public record SalesTrendView(
            java.time.LocalDate date,
            Money revenue,
            long orderCount
    ) {
    }

    public record TopProductView(
            Long productId,
            String productName,
            long unitsSold,
            Money revenue
    ) {
    }
}
