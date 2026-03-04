package com.tasklink.patterns.creational;

import com.tasklink.model.Role;
import com.tasklink.model.UserAccount;

public class FreelancerAccountProduct implements AccountProductA {
    private final String email;
    private final String passwordHash;

    public FreelancerAccountProduct(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    @Override
    public UserAccount toUserAccount(String email, String passwordHash) {
        return UserAccount.builder()
                .email(this.email)
                .password(this.passwordHash)
                .role(Role.FREELANCER)
                .build();
    }
}
