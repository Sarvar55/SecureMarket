package com.codems.securemarket.analytics.internal.application.service;

import com.codems.securemarket.analytics.internal.application.port.in.QueryAdminDashboardUseCase;
import com.codems.securemarket.analytics.internal.application.port.in.query.AdminDashboardView;
import com.codems.securemarket.analytics.internal.application.port.in.query.DashboardQuery;
import com.codems.securemarket.analytics.internal.application.port.out.LoadSalesAnalyticsPort;
import com.codems.securemarket.order.api.OrderAnalyticsSnapshot;
import com.codems.securemarket.shared.domain.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public final class AdminDashboardService implements QueryAdminDashboardUseCase {

        private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

        private final LoadSalesAnalyticsPort loadSalesAnalyticsPort;
        private final Clock clock;

        public AdminDashboardService(LoadSalesAnalyticsPort loadSalesAnalyticsPort, Clock clock) {
                this.loadSalesAnalyticsPort = loadSalesAnalyticsPort;
                this.clock = clock;
        }

        @Override
        public AdminDashboardView getDashboard(DashboardQuery query) {

                String currency = query.currency().trim().toUpperCase(Locale.ROOT);
                var periodEnd = clock.instant();
                var periodStart = periodEnd.minus(query.days(), ChronoUnit.DAYS);
                var previousPeriodStart = periodStart.minus(query.days(), ChronoUnit.DAYS);

                OrderAnalyticsSnapshot data = loadSalesAnalyticsPort.load(
                                periodStart, periodEnd, previousPeriodStart, currency, query.topProductLimit());

                var averageOrderValue = data.currentOrderCount() == 0
                                ? BigDecimal.ZERO
                                : data.currentRevenue().divide(
                                                BigDecimal.valueOf(data.currentOrderCount()), 2, RoundingMode.HALF_UP);

                var summary = new AdminDashboardView.SalesSummaryView(
                                money(data.currentRevenue(), currency),
                                data.currentOrderCount(),
                                data.pendingOrderCount(),
                                data.failedOrderCount(),
                                data.deliveredOrderCount(),
                                money(averageOrderValue, currency));
                var velocity = new AdminDashboardView.SalesVelocityView(
                                money(perDay(data.currentRevenue(), query.days()), currency),
                                perDay(BigDecimal.valueOf(data.currentOrderCount()), query.days()));
                var changes = new AdminDashboardView.MetricChangeView(
                                percentageChange(data.currentRevenue(), data.previousRevenue()),
                                percentageChange(
                                                BigDecimal.valueOf(data.currentOrderCount()),
                                                BigDecimal.valueOf(data.previousOrderCount())));
                var trend = data.salesTrend().stream()
                                .map(point -> new AdminDashboardView.SalesTrendView(
                                                point.date(), money(point.revenue(), currency), point.orderCount()))
                                .toList();
                var products = data.topProducts().stream()
                                .map(product -> new AdminDashboardView.TopProductView(
                                                product.productId(), product.productName(), product.unitsSold(),
                                                money(product.revenue(), currency)))
                                .toList();

                return new AdminDashboardView(
                                periodStart, periodEnd, periodEnd, currency,
                                summary, velocity, changes, trend, products);
        }

        private BigDecimal perDay(BigDecimal value, int days) {
                return value.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP);
        }

        private BigDecimal percentageChange(BigDecimal current, BigDecimal previous) {
                if (previous.signum() == 0) {
                        return current.signum() == 0 ? BigDecimal.ZERO.setScale(2) : ONE_HUNDRED.setScale(2);
                }
                return current.subtract(previous)
                                .multiply(ONE_HUNDRED)
                                .divide(previous, 2, RoundingMode.HALF_UP);
        }

        private Money money(BigDecimal amount, String currency) {
                return new Money(amount, currency);
        }
}
