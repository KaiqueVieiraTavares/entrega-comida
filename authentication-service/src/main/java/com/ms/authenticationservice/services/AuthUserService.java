package com.ms.authenticationservice.services;

import com.example.sharedfilesmodule.dtos.user.UserRegisterDto;
import com.ms.authenticationservice.clients.UserServiceClient;
import com.ms.authenticationservice.dtos.user.UserLoginDto;
import com.ms.authenticationservice.dtos.user.UserResponseLoginDto;
import com.ms.authenticationservice.exceptions.user.UserEmailAlreadyExistsException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;
    private final TokenService tokenService;

    public AuthUserService(PasswordEncoder passwordEncoder, UserServiceClient userServiceClient, TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userServiceClient = userServiceClient;
        this.tokenService = tokenService;
    }


    public void registerUser(UserRegisterDto userRegisterDto){
        if(userServiceClient.existsByEmail(userRegisterDto.email())){
            throw new UserEmailAlreadyExistsException("Email already exists");
        }
        //envia para a rota de cadastro do user-service
        userServiceClient.registerUser(userRegisterDto);
    }

    public UserResponseLoginDto loginUser(UserLoginDto userLoginDto){
        var savedUser = userServiceClient.findByEmail(userLoginDto.email());
        if(!passwordEncoder.matches(userLoginDto.password(), savedUser.hashPassword())){
            throw new BadCredentialsException("invalid password");
        }
        var token = tokenService.generateToken(savedUser.email());

        return new UserResponseLoginDto(savedUser.email(), token);
    }
}
