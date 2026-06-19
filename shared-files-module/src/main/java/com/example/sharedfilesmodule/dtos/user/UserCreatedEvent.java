package com.example.sharedfilesmodule.dtos.user;

import java.util.UUID;

public record UserCreatedEvent(UUID authId,
                               String username,
                               String cpf,
                               String phone,
                               String address) {
}
