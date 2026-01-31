package com.fawry.travel_destination.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fawry.travel_destination.Interfaces.IDestinationService;
import com.fawry.travel_destination.exception.ResourceNotFoundException;
import com.fawry.travel_destination.model.dtos.DestinationDTO;
import com.fawry.travel_destination.model.dtos.DestinationResponse;
import com.fawry.travel_destination.model.entities.Destination;
import com.fawry.travel_destination.model.entities.User;
import com.fawry.travel_destination.model.entities.UserDestination;
import com.fawry.travel_destination.repository.DestinationRepository;
import com.fawry.travel_destination.repository.UserDestinationRepository;
import com.fawry.travel_destination.repository.UserRepository;

@Service
public class DestinationService  implements IDestinationService
 {
    private final DestinationRepository destinationRepository;
    private final UserDestinationRepository userDestinationRepository;
    private final UserRepository userRepository;
    DestinationService(DestinationRepository destinationRepository ,  UserDestinationRepository userDestinationRepository , UserRepository userRepository ){
        this.destinationRepository=destinationRepository;
        this.userDestinationRepository=userDestinationRepository;
        this.userRepository=userRepository;
    }
    @Override
    public List<DestinationResponse> bulkCreate(List<DestinationDTO> request) {
        List<Destination> entities = request.stream()
                .map(this::mapToEntity)
                .toList();

        List<Destination> saved = destinationRepository.saveAll(entities);
        return saved.stream().map(this::mapToResponse).toList();
    }

    @Override
    public Page<DestinationResponse> getAll(String keyword , int page , int size) {
                Pageable pageable = PageRequest.of(page, size);
        Page<Destination> response = (keyword == null || keyword.isBlank())
                ? destinationRepository.findAll(pageable)
                : destinationRepository.search(keyword, pageable);
        return response.map(this::mapToResponse);
    }

    @Override
    public boolean bulkDelete(List<Long> ids) {
        List<Destination> toDelete = destinationRepository.findAllById(ids);
        if (toDelete.isEmpty()) return false;
        destinationRepository.deleteAll(toDelete);
        return true;
    }

    @Override
    public boolean markWantToVisit(Long userId, List<Long> destinationIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        for (Long destId : destinationIds) {
            Destination dest = destinationRepository.findById(destId)
                    .orElseThrow(() -> new ResourceNotFoundException("Destination not found: " + destId));

            boolean exists = userDestinationRepository.existsByUserIdAndDestinationId(userId, destId);
            if (!exists) {
                UserDestination ud = UserDestination.builder()
                        .user(user)
                        .destination(dest)
                        .build();
                userDestinationRepository.save(ud);
            }
        }
        return true;
    }

    @Override
    public boolean markUnWantToVisit(Long userId, List<Long> destinationIds) {
        List<UserDestination> toDelete = userDestinationRepository.findAll().stream()
                .filter(ud -> ud.getUser().getId().equals(userId) && destinationIds.contains(ud.getDestination().getId()))
                .toList();

        if (toDelete.isEmpty()) return false;
        userDestinationRepository.deleteAll(toDelete);
        return true;
    }

    @Override
    public Page<DestinationResponse> getAllMarkedWantToVisit(Long userId , String keyword,int  page , int size) {
        List<Long> markedDestIds = userDestinationRepository.findByUserId(userId)
            .stream()
            .map(ud -> ud.getDestination().getId())
            .toList();
          Pageable pageable=PageRequest.of(page, size);
    Page<Destination> response = destinationRepository.findAllByIdInWithKeyword(markedDestIds, keyword, pageable);

        return response.map(this::mapToResponse);
    }

    private Destination mapToEntity(DestinationDTO dto) {
        return Destination.builder()
                .country(dto.getCountry())
                .capital(dto.getCapital())
                .region(dto.getRegion())
                .currency(dto.getCurrency())
                .population(dto.getPopulation())
                .flagUrl(dto.getFlagUrl())
                .build();
    }

    private DestinationResponse mapToResponse(Destination d) {
        return new DestinationResponse(
                d.getId(), d.getCountry(), d.getCapital(),
                d.getRegion(), d.getCurrency(), d.getPopulation(),
                d.getFlagUrl()
        );
    }

}
