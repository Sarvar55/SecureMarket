package com.codems.securemarket.analytics.internal.adapter.in.decorator;

import com.codems.securemarket.analytics.internal.application.port.in.QueryAdminDashboardUseCase;
import com.codems.securemarket.analytics.internal.application.port.in.query.AdminDashboardView;
import com.codems.securemarket.analytics.internal.application.port.in.query.DashboardQuery;
import com.codems.securemarket.analytics.internal.application.service.AdminDashboardService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Component
class TransactionalAdminDashboardDecorator implements QueryAdminDashboardUseCase {

    private final AdminDashboardService delegate;

    TransactionalAdminDashboardDecorator(AdminDashboardService delegate) {
        this.delegate = delegate;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardView getDashboard(DashboardQuery query) {
        return delegate.getDashboard(query);
    }
}
