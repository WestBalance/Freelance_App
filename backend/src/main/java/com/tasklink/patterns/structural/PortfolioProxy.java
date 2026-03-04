package com.tasklink.patterns.structural;

import com.tasklink.model.FreelancerProfile;

import java.util.List;

public class PortfolioProxy implements PortfolioView {
    private final FreelancerProfile profile;
    private PortfolioView real;

    public PortfolioProxy(FreelancerProfile profile) {
        this.profile = profile;
    }

    @Override
    public List<String> getLinks() {
        if (real == null) {
            real = new RealPortfolio(profile.getPortfolioLinks());
        }
        return real.getLinks();
    }
}
