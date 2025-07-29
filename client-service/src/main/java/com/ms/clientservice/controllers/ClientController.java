package com.ms.clientservice.controllers;


import com.ms.clientservice.auth.AuthUtil;
import com.ms.clientservice.dtos.ResponseDto;
import com.ms.clientservice.dtos.UpdateDto;
import com.ms.clientservice.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PutMapping("/me")
    public ResponseEntity<ResponseDto> updateClient(@RequestBody @Valid UpdateDto updateDto){
        UUID id = AuthUtil.getLoggedUserId();
        return ResponseEntity.ok(clientService.updateClient(updateDto, id));
    }
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteClient() {
        UUID id = AuthUtil.getLoggedUserId();
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/me")
    public ResponseEntity<ResponseDto> getClient(){
        UUID id = AuthUtil.getLoggedUserId();
        return ResponseEntity.ok(clientService.getClient(id));
    }
    @GetMapping
    public ResponseEntity<List<ResponseDto>> getAllClients(){
        return ResponseEntity.ok(clientService.getAllClients());
    }
}
