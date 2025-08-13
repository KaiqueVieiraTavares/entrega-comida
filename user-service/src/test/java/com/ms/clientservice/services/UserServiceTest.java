package com.ms.clientservice.services;

import com.ms.clientservice.dtos.RegisterDto;
import com.ms.clientservice.dtos.ResponseDto;
import com.ms.clientservice.dtos.UpdateDto;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


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
    private UserEntity expectedUserEntity;
    private UserResponseDto expectedResponseDto;
    private UpdateDto expectedUpdatedto;
    private ResponseDto expectedResponse;
    @BeforeEach
    public void setup() {
        expectedRegisterDto = new RegisterDto(username, email,cpf, password, phone);
        expectedUserEntity = new UserEntity(
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
        expectedResponse = new ResponseDto(username,email,phone);
        expectedUpdatedto = new UpdateDto(username,email);
        expectedResponseDto = new UserResponseDto(UUID.randomUUID(), username,password,List.of("USER"));
    }
    @Test
    void registerUser_ShouldReturnUserResponse() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encoded123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedUserEntity);
        when(modelMapper.map(expectedUserEntity, UserResponseDto.class)).thenReturn(expectedResponseDto);

        // Act
        UserResponseDto result = userService.registerUser(expectedRegisterDto);

        // Assert
        assertEquals(expectedResponseDto, result);
        verify(passwordEncoder).encode(expectedRegisterDto.password());
        verify(userRepository).save(any(UserEntity.class));
    }


    @Test
    void updateUser_ShouldReturnUserResponse(){
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(expectedUserEntity));

       doAnswer(invocationOnMock ->{
           UpdateDto source = invocationOnMock.getArgument(0);
           UserEntity destination = invocationOnMock.getArgument(1);
               destination.setUsername(source.name());
       destination.setEmail(source.email());
       return null;
       }).when(modelMapper).map(expectedUpdatedto, expectedUserEntity);
       when(userRepository.save(any(UserEntity.class))).thenReturn(expectedUserEntity);
       when(modelMapper.map(expectedUserEntity, ResponseDto.class)).thenReturn(expectedResponse);

       var result = userService.updateClient(expectedUpdatedto,UUID.randomUUID());

        assertEquals(expectedResponse, result);
        verify(userRepository,times(1)).findById(any(UUID.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }
}


