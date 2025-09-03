package com.ms.clientservice.controllers;

import com.example.sharedfilesmodule.dtos.user.UserRegisterDto;
import com.ms.clientservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/users")
public class InternalController {

private final UserService userService;

    public InternalController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDto> findByEmail(@PathVariable String email){
        var userResponse = userService.findByEmail(email);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email){
        var exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegisterDto registerDto){
        var response = userService.registerUser(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{userId}/address")
    public String getAddress(@PathVariable UUID userId){
        return userService.getAddress(userId);
    }
}
