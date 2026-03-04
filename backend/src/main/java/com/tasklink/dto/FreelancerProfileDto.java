package com.tasklink.dto;

import java.util.List;

public record FreelancerProfileDto(
        Long id,
        Long userId,
        String about,
        Double rating,
        List<String> skills,
        List<String> portfolioLinks,
        List<String> reviews
) {}
