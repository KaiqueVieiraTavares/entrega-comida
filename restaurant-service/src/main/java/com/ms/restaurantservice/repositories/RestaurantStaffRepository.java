package com.ms.restaurantservice.repositories;

import com.ms.restaurantservice.entities.RestaurantStaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RestaurantStaffRepository extends JpaRepository<RestaurantStaffEntity, UUID> {
    boolean existsByRestaurantIdAndUserId(UUID restaurantId, UUID userId);

    Optional<RestaurantStaffEntity> findByUserId(UUID userId);

    List<RestaurantStaffEntity> findAllByRestaurantId(UUID restaurantId);
}
