package com.ms.clientservice.controllers;

import com.ms.clientservice.dtos.RegisterDto;
import com.ms.clientservice.dtos.UserResponseDto;
import com.ms.clientservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RequestMapping("/users")
public class InternalController {

private final UserService userService;

    public InternalController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDto> findByEmail(@PathVariable String email){
        var userResponse = userService.findByUsername(email);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/email-exists")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email){
        var exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody RegisterDto registerDto){
        var response = userService.registerUser(registerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
