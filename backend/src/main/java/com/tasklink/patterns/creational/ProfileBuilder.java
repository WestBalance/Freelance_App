package com.tasklink.patterns.creational;

import com.tasklink.model.FreelancerProfile;
import com.tasklink.model.Skill;

import java.util.List;

public interface ProfileBuilder {
    void buildAbout(String about);
    void buildSkills(List<Skill> skills);
    void buildPortfolio(List<String> portfolioLinks);
    void buildReviews(List<String> reviews);
    void buildRating(Double rating);
    FreelancerProfile getResult();
}
