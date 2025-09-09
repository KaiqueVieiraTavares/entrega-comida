package com.ms.deliveryservice.entities;



import com.example.sharedfilesmodule.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private UUID orderId;

    private UUID restaurantId;
    private UUID userId;
    private UUID deliveryPersonId;

    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {
        this.status = DeliveryStatus.WAITING_ASSIGNMENT;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
