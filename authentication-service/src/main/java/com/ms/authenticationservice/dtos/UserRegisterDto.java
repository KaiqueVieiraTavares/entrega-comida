package com.ms.authenticationservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterDto(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String password,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone number cannot be blank")
        @Pattern(regexp = "\\d{11}", message = "Phone number must have 11 digits (ex: 11999999999)")
        String phone,

        @NotBlank(message = "CPF cannot be blank")
        @Pattern(regexp = "\\d{11}", message = "CPF must have 11 digits")
        String cpf
) {}
