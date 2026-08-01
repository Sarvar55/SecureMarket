package com.codems.securemarket.payment.internal.application.port.in;

import com.codems.securemarket.payment.internal.application.port.in.command.ProcessPaymentCommand;

public interface ProcessPaymentUseCase {

    void process(ProcessPaymentCommand command);
}

