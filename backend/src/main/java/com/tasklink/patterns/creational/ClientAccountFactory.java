package com.tasklink.patterns.creational;

public class ClientAccountFactory implements ProfileAbstractFactory {
    @Override
    public AccountProductA createProductA(String email, String passwordHash) {
        return new ClientAccountProduct(email, passwordHash);
    }

    @Override
    public AccountProductB createProductB() {
        return new ClientProfileProduct();
    }
}
