package com.codems.securemarket.analytics.internal.application.port.out;

import com.codems.securemarket.order.api.OrderAnalyticsSnapshot;
import java.time.Instant;

public interface LoadSalesAnalyticsPort {
    OrderAnalyticsSnapshot load(
            Instant currentPeriodStart,
            Instant currentPeriodEnd,
            Instant previousPeriodStart,
            String currency,
            int topProductLimit
    );
}
