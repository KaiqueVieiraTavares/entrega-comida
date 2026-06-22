package com.ms.clientservice.dtos;

import java.util.UUID;

public record ResponseDto(UUID id, String name, String phone, String address) {
}
