package com.ms.clientservice.services;


import com.example.sharedfilesmodule.dtos.user.UserCreatedEvent;
import com.ms.clientservice.dtos.ResponseDto;
import com.ms.clientservice.dtos.UpdateDto;
import com.ms.clientservice.entities.UserEntity;
import com.ms.clientservice.exceptions.UserNotFoundException;
import com.ms.clientservice.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(ModelMapper modelMapper, UserRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.modelMapper = modelMapper;
        this.userRepository = clientRepository;

        this.passwordEncoder = passwordEncoder;
    }



    @Transactional
    public ResponseDto registerUser(UserCreatedEvent dto) {
        var client = new UserEntity();
        client.setUsername(dto.username());
        client.setCpf(dto.cpf());
        client.setPhone(dto.phone());
        var savedUser = userRepository.save(client);
        return modelMapper.map(savedUser, ResponseDto.class);
    }

    @Transactional
    public ResponseDto updateClient(UpdateDto updateDto, UUID id){
        var client = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Client not found"));
        modelMapper.map(updateDto, client);
        var savedUser = userRepository.save(client);
        return modelMapper.map(savedUser, ResponseDto.class);
    }
    @Transactional
    public void deleteClient(UUID id){
        if(!userRepository.existsById(id)) throw new UserNotFoundException("Client not found");
        userRepository.deleteById(id);
    }

    public ResponseDto getUser(UUID id){
        var client = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Client not found"));
        return modelMapper.map(client, ResponseDto.class);
    }

    public List<ResponseDto> getAllUsers(){
        return userRepository.findAll().stream().map(user -> modelMapper.map(user, ResponseDto.class))
                .toList();
    }

    public String getAddress(UUID id){
         var user  =userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
         return user.getAddress();
    }
    public Boolean existsByUserId(UUID id){
        return userRepository.existsById(id);
    }
}
