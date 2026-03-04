package com.tasklink.patterns.structural;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PaymentFacade {
    private final PaymentGateway gateway = new StripeAdapter();

    public String pay(BigDecimal amount, String currency, String description) {
        return gateway.charge(amount, currency, description);
    }
}
