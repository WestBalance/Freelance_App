package com.tasklink.dto;

import com.tasklink.model.OrderCategory;
import com.tasklink.model.OrderStatus;
import com.tasklink.model.PricingMode;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderCloneDraftDto(
        Long id,
        String title,
        String description,
        OrderCategory category,
        BigDecimal budget,
        LocalDate deadline,
        Double minRating,
        PricingMode pricingMode,
        Integer estimatedHours,
        boolean urgent,
        boolean featured,
        OrderStatus status,
        LocalDate clonedAt
) {
}
