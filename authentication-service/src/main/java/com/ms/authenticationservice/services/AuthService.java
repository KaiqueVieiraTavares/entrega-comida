package com.ms.authenticationservice.services;


import com.ms.authenticationservice.clients.UserServiceClient;
import com.ms.authenticationservice.dtos.UserLoginDto;
import com.ms.authenticationservice.dtos.UserRegisterDto;
import com.ms.authenticationservice.dtos.UserResponseLoginDto;
import com.ms.authenticationservice.dtos.UserResponseRegisterDto;
import com.ms.authenticationservice.exceptions.EmailAlreadyExistsException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;
    private final TokenService tokenService;
    public AuthService(PasswordEncoder passwordEncoder, UserServiceClient userServiceClient, TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userServiceClient = userServiceClient;
        this.tokenService = tokenService;
    }

    public void registerUser(UserRegisterDto userRegisterDto){
        if(userServiceClient.existsByUsername(userRegisterDto.username())){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        var user = new UserRegisterDto(userRegisterDto.username(), passwordEncoder.encode(userRegisterDto.password()),
                userRegisterDto.email(), userRegisterDto.phone(), userRegisterDto.cpf());
        userServiceClient.registerUser(user);
    }

    public UserResponseLoginDto loginUser(UserLoginDto userLoginDto){
        var user = userServiceClient.findByEmail(userLoginDto.email());
        if(!passwordEncoder.matches(userLoginDto.password(), user.password())){
            throw new BadCredentialsException("invalid password");
        }
        var token = tokenService.generateToken(user.email());

        return new UserResponseLoginDto(user.email(), user.password(), token);
    }
}
