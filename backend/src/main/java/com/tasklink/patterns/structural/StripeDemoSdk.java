package com.tasklink.patterns.structural;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class StripeDemoSdk {
    private final RestClient http;
    private final String secretKey;

    public StripeDemoSdk(String apiBaseUrl, String secretKey) {
        this.http = RestClient.builder().baseUrl(apiBaseUrl == null || apiBaseUrl.isBlank() ? "https://api.stripe.com/v1" : apiBaseUrl).build();
        this.secretKey = secretKey;
    }

    public String makePayment(BigDecimal amount, String currency, String note) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("Stripe secret key is not configured. Set stripe.secret-key");
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("amount", toCents(amount));
        form.add("currency", currency == null || currency.isBlank() ? "usd" : currency.toLowerCase());
        form.add("confirm", "true");
        form.add("automatic_payment_methods[enabled]", "true");
        form.add("description", note);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<Map> response = http.post()
                .uri("/payment_intents")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(form)
                .retrieve()
                .toEntity(Map.class);

        Object id = response.getBody() == null ? null : response.getBody().get("id");
        if (id == null) {
            throw new IllegalStateException("Stripe did not return payment intent id");
        }
        return id.toString();
    }

    public String createCheckoutSession(BigDecimal amount, String currency, String note, String successUrl, String cancelUrl) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("Stripe secret key is not configured. Set stripe.secret-key");
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("mode", "payment");
        form.add("success_url", successUrl);
        form.add("cancel_url", cancelUrl);
        form.add("line_items[0][price_data][currency]", currency == null || currency.isBlank() ? "usd" : currency.toLowerCase());
        form.add("line_items[0][price_data][unit_amount]", toCents(amount));
        form.add("line_items[0][price_data][product_data][name]", "TaskLink order payment");
        form.add("line_items[0][price_data][product_data][description]", note);
        form.add("line_items[0][quantity]", "1");

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<Map> response = http.post()
                .uri("/checkout/sessions")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(form)
                .retrieve()
                .toEntity(Map.class);

        Object url = response.getBody() == null ? null : response.getBody().get("url");
        if (url == null) {
            throw new IllegalStateException("Stripe did not return checkout url");
        }
        return url.toString();
    }

    private String toCents(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).toPlainString();
    }
}
