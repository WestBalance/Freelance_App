package com.tasklink.patterns.creational;

import com.tasklink.model.Role;
import com.tasklink.model.UserAccount;

public class FreelancerAccountProduct implements AccountProductA {
    @Override
    public UserAccount toUserAccount(String email, String passwordHash) {
        return UserAccount.builder()
                .email(email)
                .password(passwordHash)
                .role(Role.FREELANCER)
                .build();
    }
}
