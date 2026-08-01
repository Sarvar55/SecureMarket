package com.codems.securemarket.audit.internal.adapter.in.web;

import com.codems.securemarket.audit.internal.application.port.in.QueryAuditUseCase;
import com.codems.securemarket.audit.internal.application.port.in.query.AuditView;
import com.codems.securemarket.shared.constants.ApplicationConstants;
import com.codems.securemarket.shared.web.response.BaseResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(value = "/admin/audit-events", version = ApplicationConstants.DEFAULT_API_VERSION)
class AdminAuditController {

    private final QueryAuditUseCase queryAuditUseCase;

    AdminAuditController(QueryAuditUseCase queryAuditUseCase) {
        this.queryAuditUseCase = queryAuditUseCase;
    }

    @GetMapping
    BaseResponse<List<AuditView>> getRecent(
            @RequestParam(defaultValue = "100") @Min(1) @Max(200) int limit
    ) {
        return BaseResponse.success(queryAuditUseCase.getRecent(limit));
    }
}
