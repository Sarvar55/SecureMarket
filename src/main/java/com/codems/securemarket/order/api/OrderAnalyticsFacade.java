package com.codems.securemarket.order.api;

import java.time.Instant;

public interface OrderAnalyticsFacade {

    OrderAnalyticsSnapshot getAnalytics(
            Instant currentPeriodStart,
            Instant currentPeriodEnd,
            Instant previousPeriodStart,
            String currency,
            int topProductLimit
    );
}
