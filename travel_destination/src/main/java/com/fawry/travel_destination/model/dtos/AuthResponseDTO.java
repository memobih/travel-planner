package com.fawry.travel_destination.model.dtos;

import com.fawry.travel_destination.model.enums.Role;

public class AuthResponseDTO  {
     private String username;
    private Role role;
    private String token;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String username, Role role, String token) {
        this.username = username;
        this.role = role;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
