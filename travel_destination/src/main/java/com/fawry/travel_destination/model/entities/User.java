package com.fawry.travel_destination.model.entities;

import java.util.List;

import com.fawry.travel_destination.model.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity 
@Table(name="users")
public class User {
 
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  

    @Column(nullable = false , unique =  true )
    private String  userName ;
    @Column(nullable = false  )
    private String password;
    @Column(nullable = false)
    private Role role;
    public User() {}

    public User(String userName, String password, Role role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    } 
     public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    @OneToMany(mappedBy = "user", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<UserDestination> userDestinations = new java.util.ArrayList<>();

    public List<UserDestination> getUserDestinations() {
        return userDestinations;
    }

    public void setUserDestinations(List<UserDestination> userDestinations) {
        this.userDestinations = userDestinations;
    }
    
}
