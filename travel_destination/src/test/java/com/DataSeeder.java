package com;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fawry.travel_destination.model.entities.User;
import com.fawry.travel_destination.model.enums.Role;
import com.fawry.travel_destination.repository.UserRepository;


@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUserName("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setRole(Role.ADMIN); 
            userRepository.save(admin);
        } else {
            System.out.println("Admin user already exists!");
        }
    }
}

