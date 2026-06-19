package com.ms.authenticationservice.services.user;

import com.example.sharedfilesmodule.enums.Role;
import com.ms.authenticationservice.dtos.user.UserRegisterDto;
import com.ms.authenticationservice.mapper.UserMapper;
import com.ms.authenticationservice.messaging.UserEventProducer;
import com.ms.authenticationservice.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthService authService;
    private final UserEventProducer userEventProducer;
    private final UserMapper userMapper;

    @Transactional
    public UUID registerUser(UserRegisterDto userRegisterDto){
       var savedUser = authService.createAuthCredentials(userRegisterDto.email(), userRegisterDto.password(), Role.USER);
        userEventProducer.publishUserCreatedEvent(userMapper.toUserCreatedEvent(savedUser.getId(), userRegisterDto));
        return savedUser.getId();
    }
}
