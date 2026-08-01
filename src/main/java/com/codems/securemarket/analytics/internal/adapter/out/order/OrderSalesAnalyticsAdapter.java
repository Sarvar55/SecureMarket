package com.codems.securemarket.analytics.internal.adapter.out.order;

import com.codems.securemarket.analytics.internal.application.port.out.LoadSalesAnalyticsPort;
import com.codems.securemarket.order.api.OrderAnalyticsFacade;
import com.codems.securemarket.order.api.OrderAnalyticsSnapshot;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
class OrderSalesAnalyticsAdapter implements LoadSalesAnalyticsPort {

    private final OrderAnalyticsFacade orderAnalyticsFacade;

    OrderSalesAnalyticsAdapter(OrderAnalyticsFacade orderAnalyticsFacade) {
        this.orderAnalyticsFacade = orderAnalyticsFacade;
    }

    @Override
    public OrderAnalyticsSnapshot load(
            Instant currentPeriodStart,
            Instant currentPeriodEnd,
            Instant previousPeriodStart,
            String currency,
            int topProductLimit
    ) {
        return orderAnalyticsFacade.getAnalytics(
                currentPeriodStart,
                currentPeriodEnd,
                previousPeriodStart,
                currency,
                topProductLimit
        );
    }
}
