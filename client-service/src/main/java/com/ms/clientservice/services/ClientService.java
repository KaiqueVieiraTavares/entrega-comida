package com.ms.clientservice.services;



import com.ms.clientservice.dtos.ResponseDto;
import com.ms.clientservice.dtos.UpdateDto;
import com.ms.clientservice.exceptions.ClientNotFoundException;
import com.ms.clientservice.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {
    private final ModelMapper modelMapper;
    private final ClientRepository clientRepository;

    public ClientService(ModelMapper modelMapper, ClientRepository clientRepository) {
        this.modelMapper = modelMapper;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public ResponseDto updateClient(UpdateDto updateDto, UUID id){
        var client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client not found"));
        modelMapper.map(updateDto, client);
        var savedUser = clientRepository.save(client);
        return modelMapper.map(savedUser, ResponseDto.class);
    }
    @Transactional
    public void deleteClient(UUID id){
        if(!clientRepository.existsById(id)) throw new ClientNotFoundException("Client not found");
        clientRepository.deleteById(id);
    }

    public ResponseDto getClient(UUID id){
        var client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client not found"));
        return modelMapper.map(client, ResponseDto.class);
    }

    public List<ResponseDto> getAllClients(){
        return clientRepository.findAll().stream().map(client -> modelMapper.map(client, ResponseDto.class))
                .toList();
    }

}
