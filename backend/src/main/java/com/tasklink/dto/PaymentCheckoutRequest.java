package com.tasklink.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentCheckoutRequest(
        @NotNull Long orderId,
        @NotBlank String successUrl,
        @NotBlank String cancelUrl
) {}
