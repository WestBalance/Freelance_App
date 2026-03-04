package com.tasklink.patterns.creational;

import com.tasklink.model.FreelancerProfile;
import com.tasklink.model.UserAccount;

public interface AccountProductB {
    FreelancerProfile toFreelancerProfile(UserAccount user);
}
