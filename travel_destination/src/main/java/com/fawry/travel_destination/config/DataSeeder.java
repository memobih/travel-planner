package com.fawry.travel_destination.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.fawry.travel_destination.model.entities.User;
import com.fawry.travel_destination.model.enums.Role;
import com.fawry.travel_destination.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        final String adminUsername = "admin"; // change if needed
        final String adminPassword = "admin123"; // change if needed

        if (userRepository.findByUserName(adminUsername).isEmpty()) {
            try {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPassword(adminPassword);
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                log.info("Admin user '{}' created by DataSeeder", adminUsername);
            } catch (DataIntegrityViolationException ex) {
                log.warn("Could not create admin user (possible race or constraint): {}", ex.getMessage());
            } catch (Exception ex) {
                log.error("Unexpected error while creating admin user", ex);
            }
        } else {
            log.info("Admin user '{}' already exists", adminUsername);
        }
    }
}
