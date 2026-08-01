package com.codems.securemarket.payment.internal.adapter.in.event;

import com.codems.securemarket.order.api.event.PaymentRequestedEvent;
import com.codems.securemarket.payment.internal.application.port.in.ProcessPaymentUseCase;
import com.codems.securemarket.payment.internal.application.port.in.command.ProcessPaymentCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
class PaymentRequestedEventListener {

    private final ProcessPaymentUseCase processPaymentUseCase;

    PaymentRequestedEventListener(ProcessPaymentUseCase processPaymentUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
    }

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT,
            fallbackExecution = true
    )
    public void on(PaymentRequestedEvent event) {
        processPaymentUseCase.process(new ProcessPaymentCommand(
                event.orderId(),
                event.customerId(),
                event.amount(),
                event.currency()
        ));
    }
}

