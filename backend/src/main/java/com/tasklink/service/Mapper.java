package com.tasklink.service;

import com.tasklink.dto.*;
import com.tasklink.model.*;

public class Mapper {
    public static OrderDto toDto(TaskOrder o) {
        return new OrderDto(o.getId(), o.getTitle(), o.getDescription(), o.getCategory(), o.getBudget(), o.getCurrency(),
                o.getDeadline(), o.getMinRating(), o.getStatus(), o.getPricingMode(), o.isUrgent(), o.isFeatured(), o.getPriorityScore(), o.getClient().getId());
    }

    public static ProposalDto toDto(Proposal p) {
        return new ProposalDto(p.getId(), p.getOrder().getId(), p.getFreelancer().getId(), p.getPrice(), p.getMessage(), p.getStatus(), p.getAttachments(), p.getAbilities());
    }

    public static FreelancerProfileDto toDto(FreelancerProfile p, java.util.List<String> portfolio, java.util.List<String> reviews) {
        return new FreelancerProfileDto(
                p.getId(),
                p.getUser().getId(),
                p.getAbout(),
                p.getRating(),
                p.getSkills().stream().map(Skill::getName).toList(),
                portfolio,
                reviews
        );
    }
}
