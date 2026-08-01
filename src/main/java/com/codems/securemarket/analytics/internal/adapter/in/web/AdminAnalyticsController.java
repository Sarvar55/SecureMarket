package com.codems.securemarket.analytics.internal.adapter.in.web;

import com.codems.securemarket.analytics.internal.application.port.in.QueryAdminDashboardUseCase;
import com.codems.securemarket.analytics.internal.application.port.in.query.AdminDashboardView;
import com.codems.securemarket.analytics.internal.application.port.in.query.DashboardQuery;
import com.codems.securemarket.shared.constants.ApplicationConstants;
import com.codems.securemarket.shared.web.response.BaseResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(value = "/admin/analytics", version = ApplicationConstants.DEFAULT_API_VERSION)
class AdminAnalyticsController {

    private final QueryAdminDashboardUseCase queryAdminDashboardUseCase;

    AdminAnalyticsController(QueryAdminDashboardUseCase queryAdminDashboardUseCase) {
        this.queryAdminDashboardUseCase = queryAdminDashboardUseCase;
    }

    @GetMapping("/dashboard")
    BaseResponse<AdminDashboardView> getDashboard(
            @RequestParam(defaultValue = "30") @Min(1) @Max(365) int days,
            @RequestParam(defaultValue = "AZN")
            @Pattern(regexp = "(?i)[A-Z]{3}") String currency,
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) int topProductLimit
    ) {
        return BaseResponse.success(queryAdminDashboardUseCase.getDashboard(
                new DashboardQuery(days, currency, topProductLimit)
        ));
    }
}
