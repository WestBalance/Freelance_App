package com.tasklink.service;

import com.tasklink.dto.CreateUserRequest;
import com.tasklink.model.Role;
import com.tasklink.model.UserAccount;
import com.tasklink.patterns.creational.ProfileAbstractFactory;
import com.tasklink.patterns.creational.TaskLinkProfileFactory;
import com.tasklink.repository.FreelancerProfileRepository;
import com.tasklink.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserAccountRepository userRepo;
    private final FreelancerProfileRepository profileRepo;
    private final PasswordEncoder passwordEncoder;
    private final TaskLinkProfileFactory factoryProvider = new TaskLinkProfileFactory();

    public UserService(UserAccountRepository userRepo, FreelancerProfileRepository profileRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.profileRepo = profileRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount create(CreateUserRequest request) {
        String email = request.email().toLowerCase();
        if (userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        String passwordHash = passwordEncoder.encode(request.password());
        ProfileAbstractFactory factory = factoryProvider.getFactory(request.role());
        UserAccount saved = userRepo.save(factory.createProductA(email, passwordHash).toUserAccount(email, passwordHash));
        if (saved.getRole() == Role.FREELANCER) {
            profileRepo.save(factory.createProductB().toFreelancerProfile(saved));
        }
        return saved;
    }
}
