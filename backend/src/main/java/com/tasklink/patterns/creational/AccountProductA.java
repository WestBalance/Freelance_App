package com.tasklink.patterns.creational;

import com.tasklink.model.UserAccount;

public interface AccountProductA {
    UserAccount toUserAccount(String email, String passwordHash);
}
