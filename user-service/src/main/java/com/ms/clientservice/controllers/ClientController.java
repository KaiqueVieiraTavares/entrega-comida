package com.ms.clientservice.controllers;


import com.ms.clientservice.dtos.RegisterDto;
import com.ms.clientservice.dtos.ResponseDto;
import com.ms.clientservice.dtos.UpdateDto;
import com.ms.clientservice.dtos.UserResponseDto;
import com.ms.clientservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class ClientController {
    private final UserService userService;

    public ClientController(UserService clientService) {
        this.userService = clientService;
    }





    @PutMapping("/me")
    public ResponseEntity<ResponseDto> updateClient(@RequestBody @Valid UpdateDto updateDto){
        UUID id = AuthUtil.getLoggedUserId();
        return ResponseEntity.ok(userService.updateClient(updateDto, id));
    }
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteClient() {
        UUID id = AuthUtil.getLoggedUserId();
        userService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/me")
    public ResponseEntity<ResponseDto> getClient(){
        UUID id = AuthUtil.getLoggedUserId();
        return ResponseEntity.ok(userService.getClient(id));
    }
    @GetMapping
    public ResponseEntity<List<ResponseDto>> getAllClients(){
        return ResponseEntity.ok(userService.getAllClients());
    }
}
