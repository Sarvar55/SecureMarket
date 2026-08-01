package com.codems.securemarket.audit.internal.adapter.in.event;

import com.codems.securemarket.audit.internal.application.port.in.RecordAuditUseCase;
import com.codems.securemarket.audit.internal.application.port.in.command.RecordAuditCommand;
import com.codems.securemarket.audit.internal.domain.model.AuditActions;
import com.codems.securemarket.audit.internal.domain.model.AuditOutcome;
import com.codems.securemarket.catalog.api.event.CategoryCreatedEvent;
import com.codems.securemarket.catalog.api.event.CategoryStatusChangedEvent;
import com.codems.securemarket.catalog.api.event.ProductCreatedEvent;
import com.codems.securemarket.catalog.api.event.ProductPriceChangedEvent;
import com.codems.securemarket.catalog.api.event.ProductStatusChangedEvent;
import com.codems.securemarket.catalog.api.event.ProductStockChangedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class CatalogAuditEventListener {

    private static final String MODULE = "CATALOG";
    private final RecordAuditUseCase audit;

    CatalogAuditEventListener(RecordAuditUseCase audit) {
        this.audit = audit;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void on(CategoryCreatedEvent event) {
        record(AuditActions.CATEGORY_CREATED, event.actorId(), "CATEGORY", event.categoryId(),
                "name=" + event.name(), event.occurredAt());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void on(CategoryStatusChangedEvent event) {
        record(AuditActions.CATEGORY_STATUS_CHANGED, event.actorId(), "CATEGORY", event.categoryId(),
                "active=" + event.active(), event.occurredAt());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void on(ProductCreatedEvent event) {
        record(AuditActions.PRODUCT_CREATED, event.actorId(), "PRODUCT", event.productId(),
                "categoryId=" + event.categoryId(), event.occurredAt());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void on(ProductPriceChangedEvent event) {
        record(AuditActions.PRODUCT_PRICE_CHANGED, event.actorId(), "PRODUCT", event.productId(),
                event.oldPrice() + " -> " + event.newPrice(), event.occurredAt());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void on(ProductStockChangedEvent event) {
        record(AuditActions.PRODUCT_STOCK_CHANGED, event.actorId(), "PRODUCT", event.productId(),
                "stock=" + event.stock() + ", reason=" + event.reason(), event.occurredAt());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void on(ProductStatusChangedEvent event) {
        record(AuditActions.PRODUCT_STATUS_CHANGED, event.actorId(), "PRODUCT", event.productId(),
                "status=" + event.status(), event.occurredAt());
    }

    private void record(
            String action,
            Long actorId,
            String resourceType,
            Long resourceId,
            String details,
            java.time.Instant occurredAt
    ) {
        audit.record(new RecordAuditCommand(
                MODULE, action, actorId, resourceType, resourceId,
                AuditOutcome.SUCCESS, details, occurredAt
        ));
    }
}
