package com.tasklink.dto;

import com.tasklink.model.OrderCategory;
import com.tasklink.model.PricingMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateOrderRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull OrderCategory category,
        @NotNull BigDecimal budget,
        LocalDate deadline,
        Double minRating,
        @NotNull PricingMode pricingMode,
        Integer estimatedHours,
        boolean urgent,
        boolean featured,
        Long clientId
) {}
