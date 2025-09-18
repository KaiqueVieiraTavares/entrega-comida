package com.ms.restaurantservice.dtos.restaurant;

import com.ms.restaurantservice.enums.Category;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RestaurantCreateDto(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @Email(message = "Email should be valid")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password cannot be blank")
        String password,

        @NotBlank(message = "CNPJ cannot be blank")
        String cnpj,

        @Pattern(regexp = "\\d{11}", message = "Phone must have 11 digits")
        String phone,

        @NotBlank(message = "Address cannot be blank")
        String address,

        @NotBlank(message = "City cannot be blank")
        String city,

        @NotBlank(message = "State cannot be blank")
        String state,

        @NotBlank(message = "CEP cannot be blank")
        String cep,

        @Size(min = 20, max = 400, message = "Description must be between 20 and 400 characters")
        String description,

        @NotNull(message = "Category cannot be null")
        Category category
) {}
