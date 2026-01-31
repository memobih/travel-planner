package com.fawry.travel_destination.controller;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fawry.travel_destination.Interfaces.IDestinationService;
import com.fawry.travel_destination.exception.ApiResponse;
import com.fawry.travel_destination.model.dtos.DestinationDTO;
import com.fawry.travel_destination.model.dtos.DestinationResponse;
import com.fawry.travel_destination.security.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
@AllArgsConstructor
@RestController
@RequestMapping("/api/destinations")


public class DestinationController {
    private final IDestinationService destinationService;

    @PreAuthorize("@authz.isAdmin(authentication)")
    @PostMapping
    public ApiResponse<List<DestinationResponse>> bulkCreateDestinations(Authentication authentication, @Valid @RequestBody List<@Valid DestinationDTO> dtos) {
        List<DestinationResponse> response = destinationService.bulkCreate(dtos);
        return new  ApiResponse<>(true , "created succeful " , response);
    }

    @PreAuthorize("@authz.isAdmin(authentication)")
    @DeleteMapping
    public ApiResponse<Boolean> bulkDeleteDestinations(@RequestBody List<Long> ids) {
        boolean deleted = destinationService.bulkDelete(ids);
        return new ApiResponse<>(true , "deleted succes " , deleted);
    }

    
    @PreAuthorize("@authz.isUserOnly(authentication) or @authz.isAdmin(authentication)")
    @GetMapping
    public ApiResponse<Page<DestinationResponse>> getAllDestinations(
          @RequestParam(defaultValue = "") String keyword
          ,
          @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<DestinationResponse> response = destinationService.getAll(keyword , page , size);
        return new  ApiResponse<> (true , "all destinations " , response);
    }

    @PreAuthorize("@authz.isUserOnly(authentication) or @authz.isAdmin(authentication)")
    @PostMapping("/want-to-visit")
    public ApiResponse<Boolean> markWantToVisit(
            @RequestBody List<Long> destinationIds,
            Authentication authentication) {

        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId(); 
        boolean success = destinationService.markWantToVisit(userId, destinationIds);
        return new ApiResponse<>(true , "marked", success);
    }

    @PreAuthorize("@authz.isUserOnly(authentication) or @authz.isAdmin(authentication)")
    @PostMapping("/unwant-to-visit")
    public ApiResponse<Boolean> markUnWantToVisit(
            @RequestBody List<Long> destinationIds,
            Authentication authentication) {

        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        boolean success = destinationService.markUnWantToVisit(userId, destinationIds);
        return new  ApiResponse<> (true, "un marked" , success);
    }

    @PreAuthorize("@authz.isUserOnly(authentication) or @authz.isAdmin(authentication)")
    @GetMapping("/want-to-visit")
    public ApiResponse<Page<DestinationResponse>> getAllMarkedWantToVisit(
          @RequestParam(defaultValue = "") String keyword
          ,
          @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ,
            Authentication authentication) {
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();
        Page<DestinationResponse> response = destinationService.getAllMarkedWantToVisit(userId , keyword  , page , size);
        return new ApiResponse<>(true , "all marked destinations " , response);
    }
}
