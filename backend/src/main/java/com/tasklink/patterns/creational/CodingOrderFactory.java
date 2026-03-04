package com.tasklink.patterns.creational;

public class CodingOrderFactory extends OrderFactoryMethod {
    @Override
    protected OrderProduct factoryMethod() {
        return new CodingOrderProduct();
    }
}
