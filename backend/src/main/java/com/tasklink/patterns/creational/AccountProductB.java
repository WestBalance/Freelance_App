package com.tasklink.patterns.creational;

import com.tasklink.model.UserAccount;
import com.tasklink.model.UserProfile;

public interface AccountProductB {
    UserProfile toProfile(UserAccount user);
}
