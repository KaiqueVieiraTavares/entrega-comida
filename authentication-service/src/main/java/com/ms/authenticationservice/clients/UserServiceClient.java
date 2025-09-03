package com.ms.authenticationservice.clients;


import com.example.sharedfilesmodule.dtos.user.UserRegisterDto;
import com.example.sharedfilesmodule.dtos.user.UserResponseDto;
import com.ms.authenticationservice.dtos.user.UserLoginDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/users/exists/{email}")
    Boolean existsByEmail(@PathVariable String email);

    @GetMapping("/users/{email}")
    UserResponseDto findByEmail(@PathVariable String email);


    @PostMapping("/users/")
    void registerUser(@RequestBody UserRegisterDto userRegisterDto);
}
