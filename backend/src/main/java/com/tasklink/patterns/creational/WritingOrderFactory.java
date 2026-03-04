package com.tasklink.patterns.creational;

public class WritingOrderFactory extends OrderFactoryMethod {
    @Override
    protected OrderProduct factoryMethod() {
        return new WritingOrderProduct();
    }
}
