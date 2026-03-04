package com.tasklink.patterns.creational;

public class ClientAccountFactory implements ProfileAbstractFactory {
    @Override
    public AccountProductA createProductA() {
        return new ClientAccountProduct();
    }

    @Override
    public AccountProductB createProductB() {
        return new ClientProfileProduct();
    }
}
