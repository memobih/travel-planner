package com.fawry.travel_destination.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fawry.travel_destination.Interfaces.IAuthService;
import com.fawry.travel_destination.exception.ApiResponse;
import com.fawry.travel_destination.model.dtos.AuthDTO;
import com.fawry.travel_destination.model.dtos.AuthResponseDTO;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }
     @PostMapping("/login")
    public ApiResponse<AuthResponseDTO> login(@Valid @RequestBody AuthDTO authDTO) {
        AuthResponseDTO response = authService.login(authDTO);
        return new ApiResponse<>(true, "User logged in successfully", response);
    }
     @PostMapping("/register")
    public ApiResponse<AuthResponseDTO> register(@Valid @RequestBody AuthDTO authDTO) {
        AuthResponseDTO response = authService.register(authDTO);
        return new ApiResponse<>(true, "User registered successfully", response);
    }
    
}
