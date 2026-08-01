package com.codems.securemarket.payment.internal.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codems.securemarket.payment.api.event.PaymentProcessedEvent;
import com.codems.securemarket.payment.api.event.PaymentStatus;
import com.codems.securemarket.payment.internal.application.port.in.command.ProcessPaymentCommand;
import com.codems.securemarket.payment.internal.application.port.out.PaymentEventPublisherPort;
import com.codems.securemarket.payment.internal.application.port.out.PaymentSimulationPort;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class PaymentServiceTest {

    private static final Instant NOW = Instant.parse("2026-08-01T09:00:00Z");

    @Test
    void successfulSimulationPublishesSucceededEvent() {
        PaymentSimulationPort simulationPort = mock(PaymentSimulationPort.class);
        PaymentEventPublisherPort eventPublisher = mock(PaymentEventPublisherPort.class);
        when(simulationPort.isSuccessful(any())).thenReturn(true);

        var service = new PaymentService(
                simulationPort,
                eventPublisher,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );

        service.process(new ProcessPaymentCommand(
                10L,
                20L,
                new BigDecimal("49.90"),
                "azn"
        ));

        var captor = ArgumentCaptor.forClass(com.codems.securemarket.shared.event.DomainEvent.class);
        verify(eventPublisher).publish(captor.capture());

        var event = (PaymentProcessedEvent) captor.getValue();
        assertEquals(PaymentStatus.SUCCEEDED, event.status());
        assertEquals("AZN", event.currency());
        assertEquals(10L, event.orderId());
    }

    @Test
    void failedSimulationPublishesFailedEvent() {
        PaymentSimulationPort simulationPort = mock(PaymentSimulationPort.class);
        PaymentEventPublisherPort eventPublisher = mock(PaymentEventPublisherPort.class);
        when(simulationPort.isSuccessful(any())).thenReturn(false);

        var service = new PaymentService(
                simulationPort,
                eventPublisher,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );

        service.process(new ProcessPaymentCommand(
                10L,
                20L,
                new BigDecimal("49.90"),
                "AZN"
        ));

        var captor = ArgumentCaptor.forClass(com.codems.securemarket.shared.event.DomainEvent.class);
        verify(eventPublisher).publish(captor.capture());

        var event = (PaymentProcessedEvent) captor.getValue();
        assertEquals(PaymentStatus.FAILED, event.status());
        assertEquals("SIMULATED_DECLINE", event.failureReason());
    }
}

