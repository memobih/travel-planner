package com.fawry.travel_destination.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("authz")
public class AuthorizationService {


    public boolean isUserOnly(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        boolean result = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_USER".equals(a.getAuthority()));
        return result;
    }

    public boolean isAdmin(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        boolean result = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
        return result;
    }
}
