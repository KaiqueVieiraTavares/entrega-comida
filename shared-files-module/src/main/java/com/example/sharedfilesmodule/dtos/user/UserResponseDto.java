package com.example.sharedfilesmodule.dtos.user;

import java.util.List;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String email,
        String hashPassword,
        List<String> roles
) {
}