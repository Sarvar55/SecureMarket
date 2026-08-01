package com.codems.securemarket.notification.internal.adapter.in.event;

import com.codems.securemarket.identity.api.event.UserRegisteredEvent;
import com.codems.securemarket.identity.api.event.UserStatusChangedEvent;
import com.codems.securemarket.notification.internal.application.port.in.command.*;
import com.codems.securemarket.notification.internal.application.port.in.CreateNotificationUseCase;
import com.codems.securemarket.notification.internal.domain.model.NotificationType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class IdentityNotificationEventListener {

    private final CreateNotificationUseCase createNotificationUseCase;

    IdentityNotificationEventListener(CreateNotificationUseCase createNotificationUseCase) {
        this.createNotificationUseCase = createNotificationUseCase;
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            fallbackExecution = true
    )
    public void on(UserRegisteredEvent event) {
        createNotificationUseCase.create(new CreateNotificationCommand(
                event.userId(),
                NotificationType.WELCOME,
                "Welcome to SecureMarket",
                "Your account has been created successfully.",
                event.occurredAt()
        ));
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            fallbackExecution = true
    )
    public void on(UserStatusChangedEvent event) {
        createNotificationUseCase.create(new CreateNotificationCommand(
                event.userId(),
                NotificationType.ACCOUNT_STATUS_CHANGED,
                "Account status updated",
                "Your account status is now " + event.currentStatus() + ".",
                event.occurredAt()
        ));
    }
}

