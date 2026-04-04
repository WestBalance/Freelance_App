package com.tasklink.patterns.structural;

import java.math.BigDecimal;

public class PaymentTransactionSubsystem {
    private final PaymentGateway gateway;

    public PaymentTransactionSubsystem(PaymentGateway gateway) {
        this.gateway = gateway;
    }

    public String performCharge(BigDecimal amount, String currency, String description) {
        return gateway.charge(amount, currency, description);
    }
}
