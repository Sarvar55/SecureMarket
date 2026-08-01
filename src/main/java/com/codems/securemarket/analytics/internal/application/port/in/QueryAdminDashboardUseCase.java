package com.codems.securemarket.analytics.internal.application.port.in;

import com.codems.securemarket.analytics.internal.application.port.in.query.AdminDashboardView;
import com.codems.securemarket.analytics.internal.application.port.in.query.DashboardQuery;

public interface QueryAdminDashboardUseCase {
    AdminDashboardView getDashboard(DashboardQuery query);
}
