package com.ms.clientservice.auth;

import java.util.UUID;

public record ResponseAuthDto(UUID id, String token, String name, String email) {
}
