package com.ms.clientservice.auth;


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
    @PostMapping("/register")
    public ResponseEntity<ResponseAuthDto> registerUser(@RequestBody RegisterDto registerDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(registerDto));
    }
    @PostMapping("/login")
    public ResponseEntity<ResponseAuthDto> loginUser(@RequestBody LoginDto loginDto){
        return ResponseEntity.ok(authService.loginUser(loginDto));
    }
}
