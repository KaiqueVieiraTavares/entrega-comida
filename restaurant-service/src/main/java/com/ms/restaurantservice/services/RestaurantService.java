package com.ms.restaurantservice.services;

import com.ms.restaurantservice.dtos.RestaurantCreateDto;
import com.ms.restaurantservice.dtos.RestaurantResponseDto;
import com.ms.restaurantservice.entities.RestaurantEntity;
import com.ms.restaurantservice.exceptions.RestaurantAlreadyExistsException;
import com.ms.restaurantservice.exceptions.RestaurantNotFoundException;
import com.ms.restaurantservice.exceptions.UnauthorizedAccessException;
import com.ms.restaurantservice.repositories.RestaurantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
        restaurant.setOwner_id(ownerId);
        var savedRestaurant = restaurantRepository.save(restaurant);
        return modelMapper.map(savedRestaurant, RestaurantResponseDto.class);
    }

    public void deleteRestaurant(UUID ownerId, UUID restaurantId){
        var restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new RestaurantNotFoundException
                ("Restaurant not found"));
        if(!(restaurant.getOwner_id().equals(ownerId))){
            throw new UnauthorizedAccessException("Not authorized to this content");
        }
        restaurantRepository.delete(restaurant);
    }
}
