package com.fawry.travel_destination.Interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fawry.travel_destination.model.dtos.DestinationDTO;
import com.fawry.travel_destination.model.dtos.DestinationResponse;

public interface IDestinationService {
    List<DestinationResponse> bulkCreate(List<DestinationDTO> request);
    Page<DestinationResponse> getAll(String keyword, int page , int size);
    boolean bulkDelete(List<Long> ids);
    boolean markWantToVisit(Long userId, List<Long> destinationIds);
   boolean markUnWantToVisit(Long userId, List<Long> destinationIds);
   Page<DestinationResponse> getAllMarkedWantToVisit(Long userId, String keyword, int page ,int size);
}
