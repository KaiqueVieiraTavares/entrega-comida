package com.ms.clientservice.services;



import com.ms.clientservice.dtos.RegisterDto;
import com.ms.clientservice.dtos.ResponseDto;
import com.ms.clientservice.dtos.UpdateDto;
import com.ms.clientservice.dtos.UserResponseDto;
import com.ms.clientservice.entities.UserEntity;
import com.ms.clientservice.enums.Role;
import com.ms.clientservice.exceptions.UserNotFoundException;
import com.ms.clientservice.exceptions.EmailAlreadyExistsException;
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
    public UserResponseDto registerUser(RegisterDto dto) {
        var client = new UserEntity();
        client.setUsername(dto.name());
        client.setEmail(dto.email());
        client.setCpf(dto.cpf());
        client.setPhone(dto.phone());
        client.setPassword(passwordEncoder.encode(dto.password()));
        client.setRole(Role.USER);

        var savedUser = userRepository.save(client);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }


    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
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

    public ResponseDto getClient(UUID id){
        var client = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Client not found"));
        return modelMapper.map(client, ResponseDto.class);
    }

    public List<ResponseDto> getAllClients(){
        return userRepository.findAll().stream().map(user -> modelMapper.map(user, ResponseDto.class))
                .toList();
    }

    public UserResponseDto findByUsername(String username){
         var user = userRepository.findByUsername(username)
                 .orElseThrow(() -> new UserNotFoundException("User not found"));
         return modelMapper.map(user, UserResponseDto.class);
    }
}
