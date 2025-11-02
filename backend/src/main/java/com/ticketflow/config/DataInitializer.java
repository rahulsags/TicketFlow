package com.ticketflow.config;

import com.ticketflow.model.Role;
import com.ticketflow.model.User;
import com.ticketflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Initializing default users...");
            
            // Create Admin
            User admin = User.builder()
                    .username("admin")
                    .email("admin@ticketflow.com")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Administrator")
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            
            // Create Support Agent
            User agent = User.builder()
                    .username("agent")
                    .email("agent@ticketflow.com")
                    .password(passwordEncoder.encode("agent123"))
                    .fullName("Support Agent")
                    .role(Role.SUPPORT_AGENT)
                    .enabled(true)
                    .build();
            userRepository.save(agent);
            
            // Create Regular User
            User user = User.builder()
                    .username("user")
                    .email("user@ticketflow.com")
                    .password(passwordEncoder.encode("user123"))
                    .fullName("Test User")
                    .role(Role.USER)
                    .enabled(true)
                    .build();
            userRepository.save(user);
            
            log.info("Default users created successfully!");
            log.info("Admin - username: admin, password: admin123");
            log.info("Agent - username: agent, password: agent123");
            log.info("User - username: user, password: user123");
        }
    }
}
