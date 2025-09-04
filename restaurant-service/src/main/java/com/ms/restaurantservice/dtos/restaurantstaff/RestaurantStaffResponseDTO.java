package com.ms.restaurantservice.dtos.restaurantstaff;
import com.ms.restaurantservice.enums.StaffRole;

import java.util.UUID;

public record RestaurantStaffResponseDTO(
        UUID id,
        UUID restaurantId,
        UUID userId,
        StaffRole role
) {}