package com.ms.deliveryservice.repositories;

import com.example.sharedfilesmodule.enums.DeliveryStatus;
import com.ms.deliveryservice.entities.DeliveryEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryEntity, UUID> {

    List<DeliveryEntity> findByStatus(DeliveryStatus status);
    List<DeliveryEntity> findByDeliveryPersonId(UUID deliveryPersonid);
}
