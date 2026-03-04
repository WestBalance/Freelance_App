package com.tasklink.config;

import com.tasklink.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordMigrationRunner implements CommandLineRunner {
    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserPasswordMigrationRunner(UserAccountRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        userRepository.findAll().forEach(user -> {
            String password = user.getPassword();
            if (password == null || !password.startsWith("$2")) {
                user.setPassword(passwordEncoder.encode(password == null ? "password" : password));
                userRepository.save(user);
            }
        });
    }
}
