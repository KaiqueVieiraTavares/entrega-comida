package com.ms.clientservice.dtos;

import java.util.List;

public record UserResponseDto(
         Long id,
         String username,
         String password,
         List<String> roles
) {
}
