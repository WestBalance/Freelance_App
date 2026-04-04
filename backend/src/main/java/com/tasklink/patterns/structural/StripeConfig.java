package com.tasklink.patterns.structural;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
public record StripeConfig(
        String secretKey,
        String apiBaseUrl
) {}
