package com.codems.securemarket.order.internal.adapter.in.facade;

import com.codems.securemarket.order.api.OrderAnalyticsFacade;
import com.codems.securemarket.order.api.OrderAnalyticsSnapshot;
import com.codems.securemarket.order.internal.application.port.out.LoadOrderAnalyticsPort;
import java.time.Instant;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class OrderAnalyticsFacadeAdapter implements OrderAnalyticsFacade {

    private final LoadOrderAnalyticsPort loadOrderAnalyticsPort;

    OrderAnalyticsFacadeAdapter(LoadOrderAnalyticsPort loadOrderAnalyticsPort) {
        this.loadOrderAnalyticsPort = loadOrderAnalyticsPort;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderAnalyticsSnapshot getAnalytics(
            Instant currentPeriodStart,
            Instant currentPeriodEnd,
            Instant previousPeriodStart,
            String currency,
            int topProductLimit
    ) {
        return loadOrderAnalyticsPort.load(
                currentPeriodStart,
                currentPeriodEnd,
                previousPeriodStart,
                currency,
                topProductLimit
        );
    }
}
