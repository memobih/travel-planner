package com.fawry.travel_destination.service;

import org.springframework.stereotype.Service;

import com.fawry.travel_destination.Interfaces.IAuthService;
import com.fawry.travel_destination.exception.FailException;
import com.fawry.travel_destination.exception.ResourceNotFoundException;
import com.fawry.travel_destination.exception.UnauthorizedException;
import com.fawry.travel_destination.model.dtos.AuthDTO;
import com.fawry.travel_destination.model.dtos.AuthResponseDTO;
import com.fawry.travel_destination.model.entities.User;
import com.fawry.travel_destination.model.enums.Role;
import com.fawry.travel_destination.repository.UserRepository;
import com.fawry.travel_destination.security.JwtTokenProvider;

@Service
public class AuthService implements IAuthService
 {
    
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    
    public AuthService(UserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Override
    public AuthResponseDTO login(AuthDTO loginDto) {

       User user = userRepository.findByUserName(loginDto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(!user.getPassword().equals(loginDto.getPassword())) {
            throw new   FailException("Invalid credentials");
        }
        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponseDTO(user.getUserName(), user.getRole(), token);
    }

    @Override
    public AuthResponseDTO register(AuthDTO registerDto) {
       
        if (userRepository.existsByUserName(registerDto.getUsername())) {
            throw new UnauthorizedException("Username already exists");
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setRole(Role.USER);
        user = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user);

        return new AuthResponseDTO(
                user.getUserName(),
                user.getRole(),
                token
        );
    }
 }
    

