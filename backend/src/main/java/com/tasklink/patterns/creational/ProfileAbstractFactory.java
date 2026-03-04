package com.tasklink.patterns.creational;

public interface ProfileAbstractFactory {
    AccountProductA createProductA(String email, String passwordHash);
    AccountProductB createProductB();
}
