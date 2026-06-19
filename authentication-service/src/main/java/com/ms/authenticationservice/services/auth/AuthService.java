package com.ms.authenticationservice.services.auth;

import com.example.sharedfilesmodule.enums.Role;
import com.ms.authenticationservice.dtos.auth.AuthLoginDto;
import com.ms.authenticationservice.dtos.auth.AuthResponseLoginDto;
import com.ms.authenticationservice.entities.UserAuth;
import com.ms.authenticationservice.exceptions.user.UserEmailAlreadyExistsException;
import com.ms.authenticationservice.exceptions.user.UserNotFoundException;
import com.ms.authenticationservice.mapper.UserMapper;
import com.ms.authenticationservice.messaging.UserEventProducer;
import com.ms.authenticationservice.repositories.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserAuthRepository userAuthRepository;
    private final UserEventProducer userEventProducer;
    private final UserMapper userMapper;






    public AuthResponseLoginDto loginUser(AuthLoginDto authLoginDto){
        UserAuth savedUser = userAuthRepository.findByEmail(authLoginDto.email()).orElseThrow(() -> new UserNotFoundException("User with email: " + authLoginDto.email() + " not found"));
        if(!passwordEncoder.matches(authLoginDto.password(), savedUser.getPasswordHash())){
            throw new BadCredentialsException("invalid password");
        }
        var token = tokenService.generateToken(savedUser.getEmail(), savedUser.getRole());
        return new AuthResponseLoginDto(savedUser.getEmail(), token);
    }
    @Transactional
    public UserAuth createAuthCredentials(String email, String password, Role role){
        if(userAuthRepository.existsByEmail(email)){
            throw new UserEmailAlreadyExistsException("Email already exists");
        }
        return userAuthRepository.save(UserAuth.builder().email(email).passwordHash(passwordEncoder.encode(password)).role(role).build());
    }

}
