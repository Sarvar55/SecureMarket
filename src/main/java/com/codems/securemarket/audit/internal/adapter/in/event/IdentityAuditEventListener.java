package com.codems.securemarket.audit.internal.adapter.in.event;

import com.codems.securemarket.audit.internal.application.port.in.RecordAuditUseCase;
import com.codems.securemarket.audit.internal.application.port.in.command.RecordAuditCommand;
import com.codems.securemarket.audit.internal.domain.model.AuditActions;
import com.codems.securemarket.audit.internal.domain.model.AuditOutcome;
import com.codems.securemarket.identity.api.event.LoginFailedEvent;
import com.codems.securemarket.identity.api.event.LoginSucceededEvent;
import com.codems.securemarket.identity.api.event.UserRegisteredEvent;
import com.codems.securemarket.identity.api.event.UserStatusChangedEvent;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
class IdentityAuditEventListener {

        private final RecordAuditUseCase recordAuditUseCase;
        private static final String SOURCE_MODULE = "IDENTITY";
        private static final String RESOURCE_TYPE = "USER";

        IdentityAuditEventListener(RecordAuditUseCase recordAuditUseCase) {
                this.recordAuditUseCase = recordAuditUseCase;
        }

        @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
        public void on(UserRegisteredEvent event) {
                log.info("User registered Event Listener triggered: {}", event.userId());
                recordAuditUseCase.record(new RecordAuditCommand(
                                SOURCE_MODULE,
                                AuditActions.REGISTER,
                                event.userId(),
                                RESOURCE_TYPE,
                                event.userId(),
                                AuditOutcome.SUCCESS,
                                null,
                                event.occurredAt()));
        }

        @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
        public void on(LoginSucceededEvent event) {
                log.info("Login succeeded Event Listener triggered: {}", event.userId());
                recordAuditUseCase.record(new RecordAuditCommand(
                                SOURCE_MODULE,
                                AuditActions.LOGIN_SUCCESS,
                                event.userId(),
                                RESOURCE_TYPE,
                                event.userId(),
                                AuditOutcome.SUCCESS,
                                null,
                                event.occurredAt()));
        }

        @EventListener
        public void on(LoginFailedEvent event) {
                log.info("Login failed Event Listener triggered: {}", event.occurredAt());
                recordAuditUseCase.record(new RecordAuditCommand(
                                SOURCE_MODULE,
                                AuditActions.LOGIN_FAILURE,
                                null,
                                RESOURCE_TYPE,
                                null,
                                AuditOutcome.FAILURE,
                                "Authentication failed",
                                event.occurredAt()));
        }

        @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
        public void on(UserStatusChangedEvent event) {
                log.info("User status changed Event Listener triggered: {}", event.userId());
                recordAuditUseCase.record(new RecordAuditCommand(
                                SOURCE_MODULE,
                                AuditActions.ROLE_ASSIGNED,
                                event.actorId(),
                                RESOURCE_TYPE,
                                event.userId(),
                                AuditOutcome.SUCCESS,
                                event.previousStatus() + " -> " + event.currentStatus(),
                                event.occurredAt()));
        }
}
