package com.fawry.travel_destination.model.dtos;

import lombok.Data;

@Data
public class DestinationDTO {
    @jakarta.validation.constraints.NotBlank(message = "Country is required")
    @jakarta.validation.constraints.Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
    private String country;

    @jakarta.validation.constraints.NotBlank(message = "Capital is required")
    @jakarta.validation.constraints.Size(min = 2, max = 100, message = "Capital must be between 2 and 100 characters")
    private String capital;

    @jakarta.validation.constraints.NotBlank(message = "Region is required")
    @jakarta.validation.constraints.Size(min = 2, max = 100, message = "Region must be between 2 and 100 characters")
    private String region;

    @jakarta.validation.constraints.NotBlank(message = "Currency is required")
    @jakarta.validation.constraints.Size(min = 1, max = 50, message = "Currency must be between 1 and 50 characters")
    private String currency;

    @jakarta.validation.constraints.NotNull(message = "Population is required")
    @jakarta.validation.constraints.Min(value = 0, message = "Population must be zero or positive")
    private Long population;

    @jakarta.validation.constraints.Size(max = 500, message = "Flag URL is too long")
    @jakarta.validation.constraints.Pattern(regexp = "^(https?://).+", message = "Flag URL must start with http:// or https://")
    private String flagUrl;
}
