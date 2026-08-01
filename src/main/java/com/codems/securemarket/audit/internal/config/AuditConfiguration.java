package com.codems.securemarket.audit.internal.config;

import com.codems.securemarket.audit.internal.application.port.out.AppendAuditPort;
import com.codems.securemarket.audit.internal.application.port.out.LoadAuditPort;
import com.codems.securemarket.audit.internal.application.service.AuditQueryService;
import com.codems.securemarket.audit.internal.application.service.AuditService;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditConfiguration {

    @Bean
    AuditService auditService(
            AppendAuditPort appendAuditPort,
            Clock clock
    ) {
        return new AuditService(appendAuditPort, clock);
    }

    @Bean
    AuditQueryService auditQueryService(LoadAuditPort loadAuditPort) {
        return new AuditQueryService(loadAuditPort);
    }
}
