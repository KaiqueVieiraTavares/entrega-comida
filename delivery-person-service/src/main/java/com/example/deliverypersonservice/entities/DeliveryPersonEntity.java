package com.example.deliverypersonservice.entities;

import com.example.deliverypersonservice.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeliveryPersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String password;
    @Email
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String cnh;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @Pattern(regexp = "^[A-Z]{3}-\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$" , message = "the plate must be in the form AAA-0000 or AAA0A00")
    @Column(name = "vehicle_plate", unique = true)
    private String vehiclePlate;
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;
    @Builder.Default
    private Boolean available=true;

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
