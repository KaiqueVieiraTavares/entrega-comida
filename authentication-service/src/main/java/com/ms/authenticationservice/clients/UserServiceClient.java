package com.ms.authenticationservice.clients;


import com.ms.authenticationservice.dtos.UserLoginDto;
import com.ms.authenticationservice.dtos.UserRegisterDto;
import com.ms.authenticationservice.dtos.UserResponseLoginDto;
import com.ms.authenticationservice.dtos.UserResponseRegisterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/users/{username}")
    Boolean existsByUsername(@PathVariable String username);

    @GetMapping("/users/{email}")
    UserLoginDto findByEmail(@PathVariable String email);

    @PostMapping("/users/")
    void registerUser(@RequestBody UserRegisterDto userRegisterDto);
}
