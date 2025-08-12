package com.ms.clientservice.services;

import com.ms.clientservice.dtos.RegisterDto;
import com.ms.clientservice.dtos.ResponseDto;
import com.ms.clientservice.dtos.UserResponseDto;
import com.ms.clientservice.entities.UserEntity;
import com.ms.clientservice.enums.Role;
import com.ms.clientservice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;
    private final String username = "Joao";
    private final String email = "joao@email.com";
    private final String password = "123456";
    private final String phone = "11999999999";
    private final String cpf = "12345678900";
    private final Role role = Role.USER;

    private RegisterDto expectedRegisterDto;
    private UserEntity savedUserEntity;
    private UserResponseDto expectedResponse;
    @BeforeEach
    public void setup() {
        expectedRegisterDto = new RegisterDto(username, email,cpf, password, phone);
        savedUserEntity = new UserEntity(
                UUID.randomUUID(),
                username,
                email,
                phone,
                cpf,
                password,
                role,
                Instant.now(),
                Instant.now()
        );
        expectedResponse = new UserResponseDto(UUID.randomUUID(), username,password,List.of("USER"));
    }
    @Test
    void registerUser_ShouldReturnUserResponse() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encoded123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUserEntity);
        when(modelMapper.map(savedUserEntity, UserResponseDto.class)).thenReturn(expectedResponse);

        // Act
        UserResponseDto result = userService.registerUser(expectedRegisterDto);

        // Assert
        assertEquals(expectedResponse, result);
        verify(passwordEncoder).encode(expectedRegisterDto.password());
        verify(userRepository).save(any(UserEntity.class));
    }
}


