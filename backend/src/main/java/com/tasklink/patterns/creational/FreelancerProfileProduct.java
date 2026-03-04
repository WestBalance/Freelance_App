package com.tasklink.patterns.creational;

import com.tasklink.model.FreelancerProfile;
import com.tasklink.model.UserAccount;
import com.tasklink.model.UserProfile;

import java.util.ArrayList;

public class FreelancerProfileProduct implements AccountProductB {
    @Override
    public UserProfile toProfile(UserAccount user) {
        return FreelancerProfile.builder()
                .user(user)
                .about("")
                .rating(0.0)
                .skills(new ArrayList<>())
                .portfolioLinks(new ArrayList<>())
                .reviews(new ArrayList<>())
                .build();
    }
}
