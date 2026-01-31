package com.fawry.travel_destination.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fawry.travel_destination.model.entities.User;

public interface UserRepository extends JpaRepository<User , Long> {

    Optional<User> findByUserName(String username);
    boolean existsByUserName(String username); 
    
}
