package com.tasklink.dto;

import com.tasklink.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderDto(
        Long id,
        String title,
        String description,
        OrderCategory category,
        BigDecimal budget,
        String currency,
        LocalDate deadline,
        Double minRating,
        OrderStatus status,
        PricingMode pricingMode,
        boolean urgent,
        boolean featured,
        Integer priorityScore,
        Long clientId,
        String clientName,
        String clientEmail
) {}
