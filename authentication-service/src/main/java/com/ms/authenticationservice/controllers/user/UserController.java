package com.ms.authenticationservice.controllers.user;

import com.ms.authenticationservice.dtos.user.UserRegisterDto;
import com.ms.authenticationservice.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UUID> registerUser(@RequestBody @Valid UserRegisterDto userRegisterDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(userRegisterDto));
    }
}
