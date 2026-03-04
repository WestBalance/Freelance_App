package com.tasklink.patterns.creational;

public class DesignOrderFactory extends OrderFactoryMethod {
    @Override
    protected OrderProduct factoryMethod() {
        return new DesignOrderProduct();
    }
}
