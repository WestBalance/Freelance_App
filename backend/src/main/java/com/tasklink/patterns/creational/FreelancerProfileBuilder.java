package com.tasklink.patterns.creational;

import com.tasklink.model.FreelancerProfile;
import com.tasklink.model.Skill;

import java.util.ArrayList;
import java.util.List;

public class FreelancerProfileBuilder implements ProfileBuilder {
    private final FreelancerProfile product;

    public FreelancerProfileBuilder(FreelancerProfile baseProfile) {
        this.product = baseProfile;
    }

    @Override
    public void buildAbout(String about) {
        product.setAbout(about == null ? "" : about);
    }

    @Override
    public void buildSkills(List<Skill> skills) {
        product.setSkills(skills == null ? new ArrayList<>() : new ArrayList<>(skills));
    }

    @Override
    public void buildPortfolio(List<String> portfolioLinks) {
        product.setPortfolioLinks(portfolioLinks == null ? new ArrayList<>() : new ArrayList<>(portfolioLinks));
    }

    @Override
    public void buildReviews(List<String> reviews) {
        product.setReviews(reviews == null ? new ArrayList<>() : new ArrayList<>(reviews));
    }

    @Override
    public void buildRating(Double rating) {
        product.setRating(rating == null ? 0.0 : rating);
    }

    @Override
    public FreelancerProfile getResult() {
        return product;
    }
}
