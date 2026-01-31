package com.fawry.travel_destination.Interfaces;

import com.fawry.travel_destination.model.dtos.AuthDTO;
import com.fawry.travel_destination.model.dtos.AuthResponseDTO;

public interface IAuthService {
    AuthResponseDTO login(AuthDTO loginDto);
    AuthResponseDTO register(AuthDTO registerDto); 
}

