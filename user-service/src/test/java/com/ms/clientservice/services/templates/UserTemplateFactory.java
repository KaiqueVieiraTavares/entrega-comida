package com.ms.clientservice.services.templates;

import com.example.sharedfilesmodule.dtos.user.UserCreatedEvent;
import com.ms.clientservice.dtos.ResponseDto;
import com.ms.clientservice.dtos.UpdateDto;
import com.ms.clientservice.entities.UserEntity;

import java.util.UUID;

public class UserTemplateFactory {


    public static final UUID ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    public static final String USERNAME = "Joao";
    public static final String ADDRESS = "Rua Exemplo, 123";
    public static final String PHONE = "11999999999";
    public static final String CPF = "12345678900";

    public static UserEntity createValidUserEntity() {
        return new UserEntity(ID, USERNAME, CPF, PHONE, ADDRESS);
    }

    public static UserCreatedEvent createValidUserCreatedEvent() {
        return new UserCreatedEvent(ID, USERNAME, CPF, PHONE, ADDRESS);
    }

    public static ResponseDto createValidResponseDto() {
        return new ResponseDto(ID, USERNAME, PHONE, ADDRESS);
    }

    public static UpdateDto createValidUpdateDto() {
        return new UpdateDto(USERNAME, ADDRESS, PHONE);
    }
}