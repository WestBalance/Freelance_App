package com.tasklink.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FreelancerProfileRequest(
        @NotNull Long userId,
        String about,
        Double rating,
        List<Long> skillIds,
        List<String> portfolioLinks,
        List<String> reviews
) {}
