package com.ms.restaurantservice.entities;

import com.ms.restaurantservice.enums.StaffRole;
import jakarta.persistence.*;
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
public class RestaurantStaffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "restaurant_id", nullable = false)
    private UUID restaurantId;
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @Column(name = "staff_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private StaffRole staffRole;
}
