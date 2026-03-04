package com.tasklink.patterns.creational;

public class FreelancerAccountFactory implements ProfileAbstractFactory {
    @Override
    public AccountProductA createProductA(String email, String passwordHash) {
        return new FreelancerAccountProduct(email, passwordHash);
    }

    @Override
    public AccountProductB createProductB() {
        return new FreelancerProfileProduct();
    }
}
