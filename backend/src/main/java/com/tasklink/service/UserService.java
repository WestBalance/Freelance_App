package com.tasklink.service;

import com.tasklink.dto.CreateUserRequest;
import com.tasklink.model.ClientProfile;
import com.tasklink.model.FreelancerProfile;
import com.tasklink.model.UserAccount;
import com.tasklink.model.UserProfile;
import com.tasklink.patterns.creational.ProfileAbstractFactory;
import com.tasklink.patterns.creational.TaskLinkProfileFactory;
import com.tasklink.repository.ClientProfileRepository;
import com.tasklink.repository.FreelancerProfileRepository;
import com.tasklink.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserAccountRepository userRepo;
    private final FreelancerProfileRepository freelancerProfileRepo;
    private final ClientProfileRepository clientProfileRepo;
    private final PasswordEncoder passwordEncoder;
    private final TaskLinkProfileFactory factoryProvider = new TaskLinkProfileFactory();

    public UserService(UserAccountRepository userRepo,
                       FreelancerProfileRepository freelancerProfileRepo,
                       ClientProfileRepository clientProfileRepo,
                       PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.freelancerProfileRepo = freelancerProfileRepo;
        this.clientProfileRepo = clientProfileRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount create(CreateUserRequest request) {
        String email = request.email().toLowerCase();
        if (userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        String passwordHash = passwordEncoder.encode(request.password());
        ProfileAbstractFactory factory = factoryProvider.getFactory(request.role());
        UserAccount saved = userRepo.save(factory.createProductA().toUserAccount(email, passwordHash));
        saveProfile(factory.createProductB().toProfile(saved));
        return saved;
    }

    private void saveProfile(UserProfile profile) {
        if (profile instanceof FreelancerProfile freelancerProfile) {
            freelancerProfileRepo.save(freelancerProfile);
            return;
        }
        if (profile instanceof ClientProfile clientProfile) {
            clientProfileRepo.save(clientProfile);
            return;
        }
        throw new IllegalArgumentException("Unsupported profile type: " + profile.getClass().getSimpleName());
    }
}
