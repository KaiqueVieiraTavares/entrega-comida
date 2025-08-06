package com.ms.authenticationservice.controllers;


import com.ms.authenticationservice.dtos.UserLoginDto;
import com.ms.authenticationservice.dtos.UserRegisterDto;
import com.ms.authenticationservice.dtos.UserResponseLoginDto;
import com.ms.authenticationservice.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/login")
    public ResponseEntity<UserResponseLoginDto> loginUser(@Valid @RequestBody UserLoginDto userLoginDto){
        var result = authService.loginUserService(userLoginDto);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto){
         authService.registerUserService(userRegisterDto);
         return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
