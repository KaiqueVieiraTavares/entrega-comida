package com.ms.authenticationservice.controllers;


import com.example.sharedfilesmodule.dtos.user.UserRegisterDto;
import com.ms.authenticationservice.dtos.user.UserLoginDto;
import com.ms.authenticationservice.dtos.user.UserResponseLoginDto;
import com.ms.authenticationservice.services.AuthUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/users")
public class AuthUserController {
    private final AuthUserService authService;

    public AuthUserController(AuthUserService authService) {
        this.authService = authService;
    }
    @PostMapping("/login")
    public ResponseEntity<UserResponseLoginDto> loginUser(@Valid @RequestBody UserLoginDto userLoginDto){
        var result = authService.loginUser(userLoginDto);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegisterDto userRegisterDto){
         authService.registerUser(userRegisterDto);
         return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
