package com.tasklink.patterns.creational;

import com.tasklink.model.Role;

public class TaskLinkProfileFactory {
    public ProfileAbstractFactory getFactory(Role role) {
        return role == Role.FREELANCER ? new FreelancerAccountFactory() : new ClientAccountFactory();
    }
}
