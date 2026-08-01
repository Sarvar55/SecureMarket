package com.codems.securemarket.audit.internal.adapter.in.event;

import com.codems.securemarket.audit.internal.application.port.in.RecordAuditUseCase;
import com.codems.securemarket.audit.internal.application.port.in.command.RecordAuditCommand;
import com.codems.securemarket.audit.internal.domain.model.AuditActions;
import com.codems.securemarket.audit.internal.domain.model.AuditOutcome;
import com.codems.securemarket.order.api.event.CheckoutStartedEvent;
import com.codems.securemarket.order.api.event.OrderCreatedEvent;
import com.codems.securemarket.order.api.event.OrderStatusChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class OrderAuditEventListener {

    private static final String MODULE = "ORDER";
    private final RecordAuditUseCase audit;

    OrderAuditEventListener(RecordAuditUseCase audit) {
        this.audit = audit;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void on(CheckoutStartedEvent event) {
        record(AuditActions.CHECKOUT_STARTED, event.customerId(), null,
                "Checkout completed successfully", event.occurredAt());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void on(OrderCreatedEvent event) {
        record(AuditActions.ORDER_CREATED, event.customerId(), event.orderId(),
                null, event.occurredAt());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void on(OrderStatusChangedEvent event) {
        Long actorId = event.actorId() == null ? event.customerId() : event.actorId();
        record(AuditActions.ORDER_STATUS_CHANGED, actorId, event.orderId(),
                event.previousStatus() + " -> " + event.currentStatus(), event.occurredAt());
    }

    private void record(
            String action,
            Long actorId,
            Long orderId,
            String details,
            java.time.Instant occurredAt
    ) {
        audit.record(new RecordAuditCommand(
                MODULE, action, actorId, "ORDER", orderId,
                AuditOutcome.SUCCESS, details, occurredAt
        ));
    }
}
