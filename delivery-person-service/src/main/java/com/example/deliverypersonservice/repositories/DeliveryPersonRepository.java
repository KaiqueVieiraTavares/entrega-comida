package com.example.deliverypersonservice.repositories;

import com.example.deliverypersonservice.entities.DeliveryPersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface DeliveryPersonRepository extends JpaRepository<DeliveryPersonEntity, UUID> {
}
