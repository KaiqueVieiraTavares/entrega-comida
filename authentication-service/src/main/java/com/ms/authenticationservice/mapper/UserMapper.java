package com.ms.authenticationservice.mapper;

import com.example.sharedfilesmodule.dtos.user.UserCreatedEvent;
import com.ms.authenticationservice.dtos.user.UserRegisterDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {

    public UserCreatedEvent toUserCreatedEvent(
            UUID authId,
            UserRegisterDto dto) {

        return new UserCreatedEvent(
                authId,
                dto.name(),
                dto.cpf(),
                dto.phone(),
                dto.address()
        );
    }
}
