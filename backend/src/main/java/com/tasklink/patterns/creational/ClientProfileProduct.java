package com.tasklink.patterns.creational;

import com.tasklink.model.FreelancerProfile;
import com.tasklink.model.UserAccount;

public class ClientProfileProduct implements AccountProductB {
    @Override
    public FreelancerProfile toFreelancerProfile(UserAccount user) {
        return null;
    }
}
