package com.codems.securemarket.analytics.internal.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codems.securemarket.analytics.internal.application.port.in.query.DashboardQuery;
import com.codems.securemarket.analytics.internal.application.port.out.LoadSalesAnalyticsPort;
import com.codems.securemarket.order.api.OrderAnalyticsSnapshot;
import com.codems.securemarket.order.api.SalesTrendSnapshot;
import com.codems.securemarket.order.api.TopProductSnapshot;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;

class AdminDashboardServiceTest {

    private static final Instant NOW = Instant.parse("2026-08-01T12:00:00Z");

    @Test
    void calculatesSummaryVelocityAndChanges() {
        LoadSalesAnalyticsPort port = mock(LoadSalesAnalyticsPort.class);
        var currentStart = Instant.parse("2026-07-22T12:00:00Z");
        var previousStart = Instant.parse("2026-07-12T12:00:00Z");
        when(port.load(currentStart, NOW, previousStart, "AZN", 5)).thenReturn(snapshot());

        var service = new AdminDashboardService(port, Clock.fixed(NOW, ZoneOffset.UTC));
        var result = service.getDashboard(new DashboardQuery(10, "azn", 5));

        assertThat(result.summary().grossRevenue().amount()).isEqualByComparingTo("1000.00");
        assertThat(result.summary().averageOrderValue().amount()).isEqualByComparingTo("100.00");
        assertThat(result.velocity().revenuePerDay().amount()).isEqualByComparingTo("100.00");
        assertThat(result.velocity().ordersPerDay()).isEqualByComparingTo("1.00");
        assertThat(result.changes().revenueChangePercentage()).isEqualByComparingTo("25.00");
        assertThat(result.changes().orderCountChangePercentage()).isEqualByComparingTo("25.00");
        assertThat(result.topProducts()).singleElement().satisfies(product -> {
            assertThat(product.productName()).isEqualTo("Keyboard");
            assertThat(product.revenue().currency()).isEqualTo("AZN");
        });
        verify(port).load(currentStart, NOW, previousStart, "AZN", 5);
    }

    @Test
    void returnsHundredPercentGrowthWhenPreviousPeriodWasEmpty() {
        LoadSalesAnalyticsPort port = mock(LoadSalesAnalyticsPort.class);
        when(port.load(
                Instant.parse("2026-07-31T12:00:00Z"),
                NOW,
                Instant.parse("2026-07-30T12:00:00Z"),
                "AZN",
                5
        )).thenReturn(new OrderAnalyticsSnapshot(
                decimal("20"), 1, 0, 0, 0,
                BigDecimal.ZERO, 0, List.of(), List.of()
        ));

        var service = new AdminDashboardService(port, Clock.fixed(NOW, ZoneOffset.UTC));
        var result = service.getDashboard(new DashboardQuery(1, "AZN", 5));

        assertThat(result.changes().revenueChangePercentage()).isEqualByComparingTo("100.00");
        assertThat(result.changes().orderCountChangePercentage()).isEqualByComparingTo("100.00");
    }

    @Test
    void rejectsUnsupportedPeriod() {
        var service = new AdminDashboardService(
                mock(LoadSalesAnalyticsPort.class), Clock.fixed(NOW, ZoneOffset.UTC)
        );

        assertThatThrownBy(() -> service.getDashboard(new DashboardQuery(366, "AZN", 5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("days must be between 1 and 365");
    }

    private OrderAnalyticsSnapshot snapshot() {
        return new OrderAnalyticsSnapshot(
                decimal("1000"), 10, 2, 1, 4,
                decimal("800"), 8,
                List.of(new SalesTrendSnapshot(LocalDate.parse("2026-08-01"), decimal("100"), 1)),
                List.of(new TopProductSnapshot(4L, "Keyboard", 3, decimal("300")))
        );
    }

    private BigDecimal decimal(String value) {
        return new BigDecimal(value);
    }
}
