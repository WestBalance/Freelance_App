package com.tasklink.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record CreateProposalRequest(
        @NotNull Long orderId,
        Long freelancerId,
        @NotNull @DecimalMin("1") BigDecimal price,
        @NotBlank String message,
        List<String> attachments
) {}
