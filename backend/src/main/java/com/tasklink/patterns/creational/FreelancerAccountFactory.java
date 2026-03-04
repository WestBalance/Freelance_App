package com.tasklink.patterns.creational;

public class FreelancerAccountFactory implements ProfileAbstractFactory {
    @Override
    public AccountProductA createProductA() {
        return new FreelancerAccountProduct();
    }

    @Override
    public AccountProductB createProductB() {
        return new FreelancerProfileProduct();
    }
}
