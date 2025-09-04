package com.ms.restaurantservice.entities;


import com.ms.restaurantservice.enums.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "owner_id")
    private UUID ownerId;
    private String name;
    @Email
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Column(unique = true, nullable = false)
    private String cnpj;
    @Pattern(regexp = "\\d{11}", message = "the number must have 11 digits ")
    private String phone;
    private String address;
    private String city;
    private String state;
    private String cep;
    @Size(min = 20, max = 400)
    private String description;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Builder.Default
    private Double rating = 5.0;
    @Builder.Default
    @Column(name = "rating_count")
    private Integer ratingCount = 0;
}
