package com.tasklink.patterns.creational;

public class MarketingOrderFactory extends OrderFactoryMethod {
    @Override
    protected OrderProduct factoryMethod() {
        return new MarketingOrderProduct();
    }
}
