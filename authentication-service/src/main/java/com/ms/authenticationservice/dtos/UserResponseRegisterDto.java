package com.ms.authenticationservice.dtos;

public record UserResponseRegisterDto(String name, String email, String password, String phone,String cpf) {
}
