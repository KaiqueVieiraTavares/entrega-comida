package com.ms.orderservice.repositories;

import com.example.sharedfilesmodule.enums.OrderStatus;
import com.ms.orderservice.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {


    List<OrderEntity> findByStatusAndExpiresAtBefore(OrderStatus status, LocalDateTime localDateTime);
}
