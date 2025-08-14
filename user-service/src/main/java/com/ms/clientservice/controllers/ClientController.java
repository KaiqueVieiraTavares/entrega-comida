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
    public ResponseEntity<ResponseDto> updateClient(@RequestHeader("X-User-Id") String userId, @RequestBody @Valid UpdateDto updateDto){

        return ResponseEntity.ok(userService.updateClient(updateDto, UUID.fromString(userId)));
    }
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteClient(@RequestHeader("X-User-Id") String userId) {
        userService.deleteClient(UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/me")
    public ResponseEntity<ResponseDto> getClient(@RequestHeader("X-User-Id") String userId){
        return ResponseEntity.ok(userService.getUser(UUID.fromString(userId)));
    }
    @GetMapping
    public ResponseEntity<List<ResponseDto>> getAllClients(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
