package com.example.deliverypersonservice.dtos;

import com.example.deliverypersonservice.enums.VehicleType;
import jakarta.validation.constraints.*;

public record DeliveryPersonRequestDTO(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "CNH is required")
        @Size(min = 11, max = 11, message = "CNH must have 11 digits")
        String cnh,

        @NotBlank(message = "Vehicle plate is required")
        @Pattern(
                regexp = "^[A-Z]{3}-\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$",
                message = "Vehicle plate must be in the format AAA-0000 or AAA0A00"
        )
        String vehiclePlate,

        @NotNull(message = "Vehicle type is required")
        VehicleType vehicleType

) {}