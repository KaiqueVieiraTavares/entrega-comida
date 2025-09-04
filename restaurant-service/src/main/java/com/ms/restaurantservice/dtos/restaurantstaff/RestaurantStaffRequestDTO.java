package com.ms.restaurantservice.dtos.restaurantstaff;
import com.ms.restaurantservice.enums.StaffRole;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RestaurantStaffRequestDTO(
        @NotNull(message = "the user id is mandatory")
        UUID userId,

        @NotNull(message = "the role is mandatory")
        StaffRole role
) {}