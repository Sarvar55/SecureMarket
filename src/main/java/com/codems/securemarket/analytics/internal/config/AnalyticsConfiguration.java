package com.codems.securemarket.analytics.internal.config;

import com.codems.securemarket.analytics.internal.application.port.out.LoadSalesAnalyticsPort;
import com.codems.securemarket.analytics.internal.application.service.AdminDashboardService;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnalyticsConfiguration {

    @Bean
    AdminDashboardService adminDashboardService(
            LoadSalesAnalyticsPort loadSalesAnalyticsPort,
            Clock clock
    ) {
        return new AdminDashboardService(loadSalesAnalyticsPort, clock);
    }
}
