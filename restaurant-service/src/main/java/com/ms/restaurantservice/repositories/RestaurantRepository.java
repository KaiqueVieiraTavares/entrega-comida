package com.ms.restaurantservice.repositories;

import com.ms.restaurantservice.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, UUID> {
    Boolean existsByName(String name);
}
