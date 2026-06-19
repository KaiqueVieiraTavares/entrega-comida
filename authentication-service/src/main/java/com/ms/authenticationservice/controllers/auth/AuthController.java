package com.ms.authenticationservice.controllers.auth;


import com.ms.authenticationservice.dtos.auth.AuthLoginDto;
import com.ms.authenticationservice.dtos.auth.AuthResponseLoginDto;
import com.ms.authenticationservice.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseLoginDto> loginUser(@Valid @RequestBody AuthLoginDto authLoginDto){
        return ResponseEntity.ok(authService.loginUser(authLoginDto));
    }

}
