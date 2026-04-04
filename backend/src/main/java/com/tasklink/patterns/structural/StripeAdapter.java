package com.tasklink.patterns.structural;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StripeAdapter implements PaymentGateway {
    private final StripeDemoSdk sdk;

    public StripeAdapter(StripeConfig config) {
        this.sdk = new StripeDemoSdk(config.apiBaseUrl(), config.secretKey());
    }

    @Override
    public String charge(BigDecimal amount, String currency, String description) {
        return sdk.makePayment(amount, currency, description);
    }

    public String createCheckoutSession(BigDecimal amount, String currency, String description, String successUrl, String cancelUrl) {
        return sdk.createCheckoutSession(amount, currency, description, successUrl, cancelUrl);
    }
}
