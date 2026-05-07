package com.tasklink.patterns.structural;

import com.tasklink.model.TaskOrder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentFacade {
    private final PaymentValidationSubsystem validationSubsystem;
    private final PaymentReceiptSubsystem receiptSubsystem;
    private final PaymentTransactionSubsystem transactionSubsystem;

    public PaymentFacade(PaymentGateway gateway) {
        this.validationSubsystem = new PaymentValidationSubsystem();
        this.receiptSubsystem = new PaymentReceiptSubsystem();
        this.transactionSubsystem = new PaymentTransactionSubsystem(gateway);
    }

    public String pay(TaskOrder order, Long payerId, BigDecimal amount) {
        validationSubsystem.validatePayable(order, payerId);
        PaymentDescriptionBridge descriptionBridge = new InstantChargeDescriptionBridge(new DefaultPaymentDescriptionImplementor());
        String description = receiptSubsystem.buildDescription(order, payerId, descriptionBridge);
        return transactionSubsystem.performCharge(amount, order.getCurrency(), description);
    }
}
