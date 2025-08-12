package com.ms.clientservice.dtos;

import java.util.List;
import java.util.UUID;

public record UserResponseDto(
         UUID id,
         String username,
         String password,
         List<String> roles
) {
}
