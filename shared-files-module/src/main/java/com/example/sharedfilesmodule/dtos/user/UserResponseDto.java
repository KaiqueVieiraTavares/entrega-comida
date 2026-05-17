package com.example.sharedfilesmodule.dtos.user;

import com.example.sharedfilesmodule.enums.Role;

import java.util.List;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String email,
        String hashPassword,
        List<Role> roles
) {
}