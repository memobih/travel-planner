package com.fawry.travel_destination.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fawry.travel_destination.model.entities.Destination;
import com.fawry.travel_destination.model.entities.User;
import com.fawry.travel_destination.model.entities.UserDestination;

public interface UserDestinationRepository extends JpaRepository<UserDestination , Long>{
      List<UserDestination> findByUser(User user);
          boolean existsByUserIdAndDestinationId(Long userId, Long destinationId);
                  List<UserDestination> findByUserId(Long userId);
    Optional<UserDestination> findByUserAndDestination(User user, Destination destination);
} 