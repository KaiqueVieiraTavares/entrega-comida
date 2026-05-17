package com.ms.restaurantservice.repositories;

import com.ms.restaurantservice.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<RestaurantEntity, UUID> {
    Boolean existsByNameAndIdNot(String name, UUID restaurantId);
    Boolean existsByName(String name);
    @Query("SELECT r.id from RestaurantEntity r where r.ownerId = :ownerId")
    Optional<UUID> findByOwnerId(UUID ownerId);
}
