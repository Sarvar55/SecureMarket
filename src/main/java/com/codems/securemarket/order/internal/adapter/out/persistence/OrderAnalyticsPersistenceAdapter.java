package com.codems.securemarket.order.internal.adapter.out.persistence;

import com.codems.securemarket.order.api.OrderAnalyticsSnapshot;
import com.codems.securemarket.order.api.SalesTrendSnapshot;
import com.codems.securemarket.order.api.TopProductSnapshot;
import com.codems.securemarket.order.internal.application.port.out.LoadOrderAnalyticsPort;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
class OrderAnalyticsPersistenceAdapter implements LoadOrderAnalyticsPort {

    private final JpaOrderRepository repository;

    OrderAnalyticsPersistenceAdapter(JpaOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrderAnalyticsSnapshot load(
            Instant currentPeriodStart,
            Instant currentPeriodEnd,
            Instant previousPeriodStart,
            String currency,
            int topProductLimit
    ) {
        OrderAnalyticsSummaryProjection summary = repository.summarize(
                currentPeriodStart, currentPeriodEnd, previousPeriodStart, currency
        );
        var trend = repository.findSalesTrend(currentPeriodStart, currentPeriodEnd, currency)
                .stream()
                .map(row -> new SalesTrendSnapshot(row.getSaleDate(), row.getRevenue(), row.getOrderCount()))
                .toList();
        var products = repository.findTopProducts(
                        currentPeriodStart, currentPeriodEnd, currency, topProductLimit
                ).stream()
                .map(row -> new TopProductSnapshot(
                        row.getProductId(), row.getProductName(), row.getUnitsSold(), row.getRevenue()
                ))
                .toList();

        return new OrderAnalyticsSnapshot(
                summary.getCurrentRevenue(),
                summary.getCurrentOrderCount(),
                summary.getPendingOrderCount(),
                summary.getFailedOrderCount(),
                summary.getDeliveredOrderCount(),
                summary.getPreviousRevenue(),
                summary.getPreviousOrderCount(),
                trend,
                products
        );
    }
}
