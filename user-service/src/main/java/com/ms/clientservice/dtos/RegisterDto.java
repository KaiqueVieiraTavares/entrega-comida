package com.ms.clientservice.dtos;

public record RegisterDto(
        String name,
        String email,
        String cpf,
        String password,
        String phone
) {}

