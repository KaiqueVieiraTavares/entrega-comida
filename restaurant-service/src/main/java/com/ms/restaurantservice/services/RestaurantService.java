package com.ms.restaurantservice.services;

import com.ms.restaurantservice.dtos.RestaurantCreateDto;
import com.ms.restaurantservice.dtos.RestaurantResponseDto;
import com.ms.restaurantservice.dtos.RestaurantUpdateDto;
import com.ms.restaurantservice.entities.RestaurantEntity;
import com.ms.restaurantservice.exceptions.RestaurantAlreadyExistsException;
import com.ms.restaurantservice.exceptions.RestaurantNotFoundException;
import com.ms.restaurantservice.exceptions.UnauthorizedAccessException;
import com.ms.restaurantservice.repositories.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    public RestaurantService(RestaurantRepository restaurantRepository, ModelMapper modelMapper) {
        this.restaurantRepository = restaurantRepository;
        this.modelMapper = modelMapper;
    }

    public RestaurantResponseDto createRestaurant( UUID ownerId,RestaurantCreateDto restaurantCreateDto){
        if(restaurantRepository.existsByName(restaurantCreateDto.name())){
            throw new RestaurantAlreadyExistsException("Restaurant already exists");
        }
        var restaurant = modelMapper.map(restaurantCreateDto, RestaurantEntity.class);
        restaurant.setOwnerId(ownerId);
        var savedRestaurant = restaurantRepository.save(restaurant);
        return modelMapper.map(savedRestaurant, RestaurantResponseDto.class);
    }
    @Transactional
    public void deleteRestaurant(UUID ownerId, UUID restaurantId){
        var restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantNotFoundException
                ("Restaurant not found"));
        if(!(restaurant.getOwnerId().equals(ownerId))){
            throw new UnauthorizedAccessException("You are not authorized to access this restaurant.");
        }
        restaurantRepository.delete(restaurant);
    }
    public RestaurantResponseDto getRestaurantById(UUID restaurantId){
        var restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
        return modelMapper.map(restaurant, RestaurantResponseDto.class);
    }
    public List<RestaurantResponseDto> getAllRestaurants(){
        return restaurantRepository.findAll().stream().map(
                restaurantEntity -> modelMapper.map(restaurantEntity,
                        RestaurantResponseDto.class)
        ).toList();
    }

    @Transactional
    public RestaurantResponseDto updateRestaurant(UUID ownerId, UUID restaurantId, RestaurantUpdateDto restaurantUpdateDto){
        var restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"));
        if(!(restaurant.getOwnerId().equals(ownerId))){
            throw new UnauthorizedAccessException("You are not authorized to access this restaurant.");
        }
        boolean isChangingName = !(restaurant.getName().equals(restaurantUpdateDto.name()));
        boolean nameExists = restaurantRepository.existsByNameAndIdNot(restaurantUpdateDto.name(), restaurantId);
        if(isChangingName && nameExists){
            throw new RestaurantAlreadyExistsException("The name of the restaurant already exists");
        }
        modelMapper.map(restaurantUpdateDto, restaurant);
        var updatedRestaurant = restaurantRepository.save(restaurant);
        return modelMapper.map(updatedRestaurant, RestaurantResponseDto.class);
    }
}
