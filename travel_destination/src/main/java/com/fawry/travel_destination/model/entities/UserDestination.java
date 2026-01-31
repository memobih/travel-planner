package com.fawry.travel_destination.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
@Entity
@Table(name = "user_destinations")
@Builder
@AllArgsConstructor
public class UserDestination {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user; 

    @ManyToOne(optional = false)
    @JoinColumn(name = "destination_id")
    private Destination destination;
    public UserDestination() {}

    public UserDestination(User user, Destination destination) {
        this.user = user;
        this.destination = destination;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Destination getDestination() {
        return destination;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

}
