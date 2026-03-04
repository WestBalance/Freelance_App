package com.tasklink.dto;

import com.tasklink.model.ProposalStatus;

import java.math.BigDecimal;
import java.util.List;

public record ProposalDto(
        Long id,
        Long orderId,
        Long freelancerId,
        BigDecimal price,
        String message,
        ProposalStatus status,
        List<String> attachments,
        List<String> abilities
) {}
