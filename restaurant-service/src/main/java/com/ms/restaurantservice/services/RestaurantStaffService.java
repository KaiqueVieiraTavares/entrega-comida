package com.ms.restaurantservice.services;

import com.ms.restaurantservice.client.UserServiceClient;
import com.ms.restaurantservice.dtos.restaurantstaff.RestaurantStaffRequestDTO;
import com.ms.restaurantservice.dtos.restaurantstaff.RestaurantStaffResponseDTO;
import com.ms.restaurantservice.entities.RestaurantStaffEntity;
import com.ms.restaurantservice.enums.StaffRole;
import com.ms.restaurantservice.exceptions.RestaurantNotFoundException;
import com.ms.restaurantservice.exceptions.UnauthorizedAccessException;
import com.ms.restaurantservice.repositories.RestaurantRepository;
import com.ms.restaurantservice.repositories.RestaurantStaffRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantStaffService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantStaffRepository restaurantStaffRepository;
    private final UserServiceClient userServiceClient;
    private final ModelMapper modelMapper;
    public RestaurantStaffService(RestaurantRepository restaurantRepository, RestaurantStaffRepository restaurantStaffRepository, UserServiceClient userServiceClient, ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantStaffRepository = restaurantStaffRepository;
        this.userServiceClient = userServiceClient;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public RestaurantStaffResponseDTO addStaff(UUID ownerId, UUID restaurantId, RestaurantStaffRequestDTO dto) {

        if (!userServiceClient.existsByUserId(dto.userId())) {
            throw new IllegalArgumentException("The user does not exist");
        }

        var restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);


        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new UnauthorizedAccessException();
        }


        if (restaurantStaffRepository.existsByRestaurantIdAndUserId(restaurantId, dto.userId())) {
            throw new IllegalArgumentException("The user is already in this restaurant");
        }


        if (dto.role() == StaffRole.OWNER) {
            throw new IllegalArgumentException("The added user cannot be an owner");
        }
        var restaurantStaff = RestaurantStaffEntity.builder()
                .restaurantId(restaurantId)
                .userId(dto.userId())
                .staffRole(dto.role())
                .build();

        var saved = restaurantStaffRepository.save(restaurantStaff);

        return modelMapper.map(saved, RestaurantStaffResponseDTO.class);
    }
    @Transactional
    public void exitFromRestaurant(UUID userId) {

        var staff = restaurantStaffRepository
                .findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("You are not in any restaurant"));

        if (staff.getStaffRole() == StaffRole.OWNER) {
            throw new IllegalArgumentException("Owner cannot exit. Delete restaurant instead.");
        }

        restaurantStaffRepository.delete(staff);

    }
    public List<RestaurantStaffResponseDTO> getAllStaffFromRestaurant(UUID userId, UUID restaurantId) {

        restaurantStaffRepository.findByUserIdAndRestaurantId(userId, restaurantId)
                .orElseThrow(() -> new UnauthorizedAccessException());

        return restaurantStaffRepository.findAllByRestaurantId(restaurantId).stream()
                .map(staff -> modelMapper.map(staff, RestaurantStaffResponseDTO.class))
                .toList();
    }

    public RestaurantStaffResponseDTO getStaffFromRestaurant(UUID userId, UUID staffId){
        var requesterStaff = restaurantStaffRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException(""));
        var staffUserTarget = restaurantStaffRepository.findByIdAndRestaurantId(staffId, requesterStaff.getRestaurantId()).orElseThrow(() ->
                new IllegalArgumentException(""));
        return modelMapper.map(staffUserTarget, RestaurantStaffResponseDTO.class);
    }

    public void removeStaffFromRestaurant(UUID userId, UUID staffId){
        var requesterStaff = restaurantStaffRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException(""));
        if(requesterStaff.getStaffRole()!=StaffRole.OWNER){
            throw new UnauthorizedAccessException();
        }
        var staffUserTarget = restaurantStaffRepository.findByIdAndRestaurantId(staffId, requesterStaff.getRestaurantId()).orElseThrow(() ->
                new IllegalArgumentException(""));
        if(requesterStaff.equals(staffUserTarget)){
            throw new IllegalArgumentException("");
        }
        restaurantStaffRepository.delete(staffUserTarget);
    }
}
