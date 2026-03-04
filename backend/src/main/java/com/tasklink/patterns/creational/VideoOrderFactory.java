package com.tasklink.patterns.creational;

public class VideoOrderFactory extends OrderFactoryMethod {
    @Override
    protected OrderProduct factoryMethod() {
        return new VideoOrderProduct();
    }
}
