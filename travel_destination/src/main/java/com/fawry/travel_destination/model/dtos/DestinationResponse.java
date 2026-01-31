package com.fawry.travel_destination.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DestinationResponse {
    private Long id;
    private String country;
    private String capital;
    private String region;
    private String currency;
    private Long population;
    private String flagUrl;
}
