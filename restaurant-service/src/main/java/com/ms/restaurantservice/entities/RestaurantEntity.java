package com.ms.restaurantservice.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity {
    @Id
    private UUID id;
    private UUID ownerId;
    private String name;
    private String email;
    private String password;
    private String cnpj;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String cep;
    private String description;
    private String category;
}
