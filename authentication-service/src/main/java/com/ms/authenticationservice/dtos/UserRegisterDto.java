package com.ms.authenticationservice.dtos;

import java.util.UUID;

public record UserRegisterDto (String username, String password, String email, String phone, String cpf){
}
