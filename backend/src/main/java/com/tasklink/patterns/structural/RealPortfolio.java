package com.tasklink.patterns.structural;

import java.util.List;

public record RealPortfolio(List<String> links) implements PortfolioView {
    @Override
    public List<String> getLinks() { return links; }
}
