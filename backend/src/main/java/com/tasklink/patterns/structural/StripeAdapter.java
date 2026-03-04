package com.tasklink.patterns.structural;

import java.math.BigDecimal;

public class StripeAdapter implements PaymentGateway {
    private final StripeDemoSdk sdk = new StripeDemoSdk();

    @Override
    public String charge(BigDecimal amount, String currency, String description) {
        return sdk.makePayment(amount, currency, description);
    }
}
