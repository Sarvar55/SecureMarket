package com.codems.securemarket.audit.internal.adapter.in.event;

import com.codems.securemarket.audit.internal.application.port.in.RecordAuditUseCase;
import com.codems.securemarket.audit.internal.application.port.in.command.RecordAuditCommand;
import com.codems.securemarket.audit.internal.domain.model.AuditActions;
import com.codems.securemarket.audit.internal.domain.model.AuditOutcome;
import com.codems.securemarket.payment.api.event.PaymentProcessedEvent;
import com.codems.securemarket.payment.api.event.PaymentStatus;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class PaymentAuditEventListener {

    private final RecordAuditUseCase recordAuditUseCase;

    private static final String SOURCE_MODULE = "PAYMENT";
    private static final String RESOURCE_TYPE = "ORDER";

    PaymentAuditEventListener(RecordAuditUseCase recordAuditUseCase) {
        this.recordAuditUseCase = recordAuditUseCase;
    }

    @EventListener
    public void on(PaymentProcessedEvent event) {
        log.info("Payment processed Event Listener triggered: {}", event.orderId());
        boolean succeeded = event.status() == PaymentStatus.SUCCEEDED;

        recordAuditUseCase.record(new RecordAuditCommand(
                SOURCE_MODULE,
                succeeded ? AuditActions.PAYMENT_SUCCEEDED : AuditActions.PAYMENT_FAILED,
                event.customerId(),
                RESOURCE_TYPE,
                event.orderId(),
                succeeded ? AuditOutcome.SUCCESS : AuditOutcome.FAILURE,
                event.failureReason(),
                event.occurredAt()));
    }
}
