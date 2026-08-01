package com.codems.securemarket.order.internal.application.port.out;

import com.codems.securemarket.order.api.OrderAnalyticsSnapshot;
import java.time.Instant;

public interface LoadOrderAnalyticsPort {

    OrderAnalyticsSnapshot load(
            Instant currentPeriodStart,
            Instant currentPeriodEnd,
            Instant previousPeriodStart,
            String currency,
            int topProductLimit
    );
}
