package com.ms.clientservice.services;

import com.ms.clientservice.dtos.RegisterDto;
import com.ms.clientservice.dtos.ResponseDto;
import com.ms.clientservice.dtos.UpdateDto;
import com.ms.clientservice.dtos.UserResponseDto;
import com.ms.clientservice.entities.UserEntity;
import com.ms.clientservice.enums.Role;
import com.ms.clientservice.exceptions.UserNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
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
    private final String address = "Rua Exemplo, 123";
    private final String email = "joao@email.com";
    private final String password = "123456";
    private final String phone = "11999999999";
    private final String cpf = "12345678900";
    private final Role role = Role.USER;

    private RegisterDto expectedRegisterDto;
    private UserEntity expectedUserEntity;
    private UserResponseDto expectedUserResponseDto;
    private UpdateDto expectedUpdatedtoDto;
    private ResponseDto expectedResponseDto;
    @BeforeEach
    public void setup() {
        expectedRegisterDto = new RegisterDto(username, email,cpf, password, phone);
        expectedUserEntity = new UserEntity(
                UUID.randomUUID(),
                username,
                address,
                email,
                phone,
                cpf,
                password,
                role,
                Instant.now(),
                Instant.now()
        );
        expectedResponseDto = new ResponseDto(username,email,phone);
        expectedUpdatedtoDto = new UpdateDto(username,email);
        expectedUserResponseDto = new UserResponseDto(UUID.randomUUID(), username,password,List.of("USER"));
    }
    @Test
    void registerUser_ShouldReturnUserResponse() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encoded123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedUserEntity);
        when(modelMapper.map(expectedUserEntity, UserResponseDto.class)).thenReturn(expectedUserResponseDto);

        // Act
        UserResponseDto result = userService.registerUser(expectedRegisterDto);

        // Assert
        assertEquals(expectedUserResponseDto, result);
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
       }).when(modelMapper).map(expectedUpdatedtoDto, expectedUserEntity);
       when(userRepository.save(any(UserEntity.class))).thenReturn(expectedUserEntity);
       when(modelMapper.map(expectedUserEntity, ResponseDto.class)).thenReturn(expectedResponseDto);

       var result = userService.updateClient(expectedUpdatedtoDto,UUID.randomUUID());

        assertEquals(expectedResponseDto, result);
        verify(userRepository,times(1)).findById(any(UUID.class));
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void updateUser_ShouldThrowAnUserNotFoundException(){
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var exception =  assertThrows(UserNotFoundException.class, () ->
                userService.updateClient(expectedUpdatedtoDto, UUID.randomUUID()));


        verify(userRepository, never()).save(any(UserEntity.class));
    }
    @Test
    void deleteUser(){
        when(userRepository.existsById(any(UUID.class))).thenReturn(Boolean.TRUE);

        userService.deleteClient(UUID.randomUUID());

        verify(userRepository,times(1)).deleteById(any(UUID.class));
    }

    @Test
    void deleteUser_ShouldThrowWhenNotFound() {
        when(userRepository.existsById(any(UUID.class))).thenReturn(Boolean.FALSE);


        assertThrows(UserNotFoundException.class,
                () -> userService.deleteClient(UUID.randomUUID())
        );

        verify(userRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void getUser_ShouldReturnResponse(){
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(expectedUserEntity));
        when(modelMapper.map(expectedUserEntity, ResponseDto.class)).thenReturn(expectedResponseDto);

        var result = userService.getUser(UUID.randomUUID());

        assertEquals(expectedResponseDto, result);
        verify(userRepository,times(1)).findById(any(UUID.class));
        verify(modelMapper,times(1)).map(expectedUserEntity, ResponseDto.class);
    }

    @Test
    void getUser_ShouldThrowWhenNotFound() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUser(UUID.randomUUID())
        );

        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {

        List<UserEntity> entities = List.of(expectedUserEntity);
        when(userRepository.findAll()).thenReturn(entities);
        when(modelMapper.map(expectedUserEntity, ResponseDto.class)).thenReturn(expectedResponseDto);


        List<ResponseDto> result = userService.getAllUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedResponseDto, result.getFirst());
        verify(userRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(expectedUserEntity, ResponseDto.class);
    }

    @Test
    void getAllUsers_ShouldReturnEmptyList_WhenNoUsersFound() {

        when(userRepository.findAll()).thenReturn(List.of());

        List<ResponseDto> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
        verify(modelMapper, never()).map(any(), any());
    }
}



