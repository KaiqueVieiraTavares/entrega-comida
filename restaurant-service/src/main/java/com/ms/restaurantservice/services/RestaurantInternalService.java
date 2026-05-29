package com.ms.restaurantservice.services;

import com.ms.restaurantservice.entities.RestaurantEntity;
import com.ms.restaurantservice.exceptions.restaurant.RestaurantNotFoundException;
import com.ms.restaurantservice.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantInternalService {
    private final RestaurantRepository restaurantRepository;


    public Boolean isRestaurantOwner(UUID restaurantId, UUID ownerId){
        return restaurantRepository.existsByIdAndOwnerId(restaurantId, ownerId);
    }
    public String getRestaurantAddress(UUID restaurantId){
        return restaurantRepository.findById(restaurantId).map(RestaurantEntity::getAddress).
                orElseThrow(RestaurantNotFoundException::new);
    }
    public UUID getRestaurantByOwnerId(UUID ownerId){
        return restaurantRepository.findByOwnerId(ownerId).orElseThrow(RestaurantNotFoundException::new);
    }
}
