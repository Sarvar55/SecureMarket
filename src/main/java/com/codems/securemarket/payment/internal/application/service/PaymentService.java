package com.codems.securemarket.payment.internal.application.service;

import com.codems.securemarket.payment.api.event.PaymentProcessedEvent;
import com.codems.securemarket.payment.api.event.PaymentStatus;
import com.codems.securemarket.payment.internal.application.port.in.ProcessPaymentUseCase;
import com.codems.securemarket.payment.internal.application.port.in.command.ProcessPaymentCommand;
import com.codems.securemarket.payment.internal.application.port.out.PaymentEventPublisherPort;
import com.codems.securemarket.payment.internal.application.port.out.PaymentSimulationPort;
import java.time.Clock;
import java.util.UUID;

public final class PaymentService implements ProcessPaymentUseCase {

    private static final String SIMULATED_DECLINE = "SIMULATED_DECLINE";

    private final PaymentSimulationPort simulationPort;
    private final PaymentEventPublisherPort eventPublisher;
    private final Clock clock;

    public PaymentService(
            PaymentSimulationPort simulationPort,
            PaymentEventPublisherPort eventPublisher,
            Clock clock
    ) {
        this.simulationPort = simulationPort;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    public void process(ProcessPaymentCommand command) {
        boolean successful = simulationPort.isSuccessful(command.amount());

        eventPublisher.publish(new PaymentProcessedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                command.orderId(),
                command.customerId(),
                command.amount(),
                command.currency(),
                successful ? PaymentStatus.SUCCEEDED : PaymentStatus.FAILED,
                successful ? null : SIMULATED_DECLINE,
                clock.instant()
        ));
    }
}

