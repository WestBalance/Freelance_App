package com.tasklink.patterns.creational;

import com.tasklink.dto.FreelancerProfileRequest;
import com.tasklink.model.Skill;

import java.util.List;

public class FreelancerProfileDirector {
    public void construct(ProfileBuilder builder, FreelancerProfileRequest request, List<Skill> skills) {
        builder.buildAbout(request.about());
        builder.buildSkills(skills);
        builder.buildPortfolio(request.portfolioLinks());
        builder.buildReviews(request.reviews());
        builder.buildRating(request.rating());
    }
}
