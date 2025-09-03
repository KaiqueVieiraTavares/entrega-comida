package com.example.deliverypersonservice.entities;

import com.example.deliverypersonservice.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String email;
    private String cnh;
    @Pattern(regexp = "^[A-Z]{3}-\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$" , message = "the plate must be in the form AAA-0000 or AAA0A00")
    @Column(name = "vehicle_plate")
    private String vehiclePlate;
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;
    @Builder.Default
    private Boolean available=true;
    @Min(0) @Max(5)
    @Builder.Default
    private Double rating =5.0;
    @Builder.Default
    @Column(name = "total_ratings")
    private Integer totalRatings = 0;
}
