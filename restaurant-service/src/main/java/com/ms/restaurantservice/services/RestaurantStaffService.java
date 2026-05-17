package com.ms.restaurantservice.services;

import com.ms.restaurantservice.dtos.restaurantstaff.RestaurantStaffRequestDTO;
import com.ms.restaurantservice.dtos.restaurantstaff.RestaurantStaffResponseDTO;
import com.ms.restaurantservice.entities.RestaurantEntity;
import com.ms.restaurantservice.entities.RestaurantStaffEntity;
import com.ms.restaurantservice.enums.StaffRole;
import com.ms.restaurantservice.exceptions.restaurant.RestaurantNotFoundException;
import com.ms.restaurantservice.exceptions.restaurant.UnauthorizedAccessException;
import com.ms.restaurantservice.exceptions.restaurantstaff.*;
import com.ms.restaurantservice.repositories.RestaurantRepository;
import com.ms.restaurantservice.repositories.RestaurantStaffRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantStaffService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantStaffRepository restaurantStaffRepository;
    private final ModelMapper modelMapper;
    public RestaurantStaffService(RestaurantRepository restaurantRepository, RestaurantStaffRepository restaurantStaffRepository, ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantStaffRepository = restaurantStaffRepository;

        this.modelMapper = modelMapper;
    }
    @Transactional
    public void createOwnerStaff(UUID restaurantId, UUID ownerId){
        var ownerStaff = RestaurantStaffEntity.builder().restaurantId(restaurantId).userId(ownerId).staffRole(StaffRole.OWNER)
                .build();
        restaurantStaffRepository.save(ownerStaff);
    }
    @Transactional
    public RestaurantStaffResponseDTO addStaff(UUID ownerId, UUID restaurantId, RestaurantStaffRequestDTO dto) {

        var restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);
        validatePermission(restaurant, ownerId);
        if (restaurantStaffRepository.existsByRestaurantIdAndUserId(restaurantId, dto.userId())) {
            throw new StaffIsAlreadyInRestaurantException("The user is already in this restaurant");
        }
        if (dto.role() == StaffRole.OWNER) {
            throw new InvalidStaffRoleException("The added user cannot be an owner");
        }

        var restaurantStaff = RestaurantStaffEntity.builder()
                .restaurantId(restaurantId)
                .userId(dto.userId())
                .staffRole(dto.role())
                .build();

        var saved = restaurantStaffRepository.save(restaurantStaff);
        return modelMapper.map(saved, RestaurantStaffResponseDTO.class);
    }

    @Transactional()
    public void exitFromRestaurant(UUID userId, UUID restaurantId) {

        var staff = restaurantStaffRepository
                .findByUserIdAndRestaurantId(userId, restaurantId)
                .orElseThrow(() -> new StaffNotFoundException("You are not in this restaurant"));

        if (staff.getStaffRole() == StaffRole.OWNER) {
            throw new OwnerCannotExitException("Owner cannot exit. Pass leadership to another user");
        }

        restaurantStaffRepository.delete(staff);

    }

    @Transactional(readOnly = true)
    public List<RestaurantStaffResponseDTO> getAllStaffFromRestaurant(UUID userId, UUID restaurantId) {

        restaurantStaffRepository.findByUserIdAndRestaurantId(userId, restaurantId)
                .orElseThrow(UnauthorizedAccessException::new);

        return restaurantStaffRepository.findAllByRestaurantId(restaurantId).stream()
                .map(staff -> modelMapper.map(staff, RestaurantStaffResponseDTO.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public RestaurantStaffResponseDTO getStaffFromRestaurant(UUID userId, UUID staffId, UUID restaurantId){
         restaurantStaffRepository.findByUserIdAndRestaurantId(userId, restaurantId).orElseThrow(UnauthorizedAccessException::new);
        var staffUserTarget = restaurantStaffRepository.findByIdAndRestaurantId(staffId, restaurantId).
                orElseThrow(() -> new StaffNotFoundException("staff not found"));
        return modelMapper.map(staffUserTarget, RestaurantStaffResponseDTO.class);
    }

    @Transactional
    public void removeStaffFromRestaurant(UUID requesterUserId, UUID staffId, UUID restaurantId){
        var restaurant = restaurantRepository.findById(restaurantId).orElseThrow(RestaurantNotFoundException::new);
        var targetStaff = restaurantStaffRepository.findByIdAndRestaurantId(staffId, restaurantId).orElseThrow(() -> new StaffNotFoundException("Staff not found"));
        validateSelfRemoval(requesterUserId, targetStaff.getUserId());
        validateOwnerRemoval(restaurant.getOwnerId(), targetStaff.getUserId());
        boolean isOwner = restaurant.getOwnerId().equals(requesterUserId), isManager = false;
        if(!isOwner){
            if(targetStaff.getStaffRole().equals(StaffRole.MANAGER)){
                throw new UnauthorizedAccessException();
            }
            var requesterStaff = restaurantStaffRepository.findByUserIdAndRestaurantId(requesterUserId, restaurantId).orElseThrow(() -> new StaffNotFoundException("Staff not found"));
            if(requesterStaff.getStaffRole().equals(StaffRole.MANAGER)){
                isManager = true;
            }
        }
        if(!isOwner && !isManager){
            throw new UnauthorizedAccessException();
        }
        restaurantStaffRepository.delete(targetStaff);
    }
    private void validatePermission(RestaurantEntity restaurantEntity, UUID ownerId){
        if (!restaurantEntity.getOwnerId().equals(ownerId)) {
            throw new UnauthorizedAccessException();
        }
    }
    private void validateSelfRemoval(UUID requesterUserId, UUID targetUserId){
        if(targetUserId.equals(requesterUserId)){
            throw new SelfRemovalNotAllowedException("You can't remove yourself");
        }
    }
    private void validateOwnerRemoval(UUID ownerId, UUID targetId){
        if(ownerId.equals(targetId)){
            throw new CannotRemoveRestaurantOwnerException("Restaurant owner cannot be removed");
        }
    }
}
