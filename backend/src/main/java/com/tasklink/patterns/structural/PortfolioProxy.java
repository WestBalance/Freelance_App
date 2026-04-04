package com.tasklink.patterns.structural;

import java.util.List;
import java.util.function.Supplier;

public class PortfolioProxy implements PortfolioView {
    private final Supplier<List<String>> linksLoader;
    private PortfolioView real;

    public PortfolioProxy(Supplier<List<String>> linksLoader) {
        this.linksLoader = linksLoader;
    }

    @Override
    public List<String> getLinks() {
        if (real == null) {
            real = new RealPortfolio(linksLoader.get());
        }
        return real.getLinks();
    }
}
