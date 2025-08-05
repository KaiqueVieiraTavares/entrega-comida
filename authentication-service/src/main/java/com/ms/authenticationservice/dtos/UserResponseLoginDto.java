package com.ms.authenticationservice.dtos;

public record UserResponseLoginDto(String email, String password, String token) {
}
