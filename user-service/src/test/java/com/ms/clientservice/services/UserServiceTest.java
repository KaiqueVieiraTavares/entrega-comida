package com.ms.clientservice.services;

import com.example.sharedfilesmodule.dtos.user.UserCreatedEvent;
import com.ms.clientservice.dtos.ResponseDto;
import com.ms.clientservice.dtos.UpdateDto;
import com.ms.clientservice.entities.UserEntity;
import com.ms.clientservice.exceptions.UserNotFoundException;
import com.ms.clientservice.repositories.UserRepository;
import com.ms.clientservice.services.templates.UserTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    private UserCreatedEvent expectedRegisterDto;
    private UserEntity expectedUserEntity;
    private UpdateDto expectedUpdatedtoDto;
    private ResponseDto expectedResponseDto;
    @BeforeEach
    public void setup() {
        expectedRegisterDto = UserTemplateFactory.createValidUserCreatedEvent();
        expectedUserEntity = UserTemplateFactory.createValidUserEntity();
        expectedResponseDto = UserTemplateFactory.createValidResponseDto();
        expectedUpdatedtoDto = UserTemplateFactory.createValidUpdateDto();
    }
    @Test
    void registerUser_ShouldReturnUserResponse() {

        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedUserEntity);
        when(modelMapper.map(expectedUserEntity, ResponseDto.class)).thenReturn(expectedResponseDto);


        ResponseDto result = userService.registerUser(expectedRegisterDto);


        assertEquals(expectedResponseDto, result);
        verify(userRepository,times(1)).save(any(UserEntity.class));
    }


    @Test
    void updateUser_ShouldReturnUserResponse(){
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(expectedUserEntity));

       doAnswer(invocationOnMock ->{
           UpdateDto source = invocationOnMock.getArgument(0);
           UserEntity destination = invocationOnMock.getArgument(1);
               destination.setUsername(source.name());
               destination.setPhone(source.phone());
               destination.setAddress(source.address());
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
        assertThrows(UserNotFoundException.class, () ->
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



