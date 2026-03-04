package com.tasklink.patterns.creational;

import com.tasklink.model.ClientProfile;
import com.tasklink.model.UserAccount;
import com.tasklink.model.UserProfile;

public class ClientProfileProduct implements AccountProductB {
    @Override
    public UserProfile toProfile(UserAccount user) {
        return ClientProfile.builder()
                .user(user)
                .companyInfo("")
                .build();
    }
}
