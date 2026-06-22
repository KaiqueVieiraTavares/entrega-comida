package com.ms.clientservice.controllers;

import com.ms.clientservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/users")
@RequiredArgsConstructor
public class InternalController {

private final UserService userService;


    @GetMapping("/{userId}/address")
    public String getAddress(@PathVariable UUID userId){
        return userService.getAddress(userId);
    }
    @GetMapping("/{userId}/exists")
    public Boolean existsByUserId(@PathVariable("userId") UUID userId){
        return userService.existsByUserId(userId);
    }
}
