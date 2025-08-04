package com.ms.authservice.Controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/success")
    public ResponseEntity<String> oauth2Success(@AuthenticationPrincipal OAuth2User principal){
        var email = principal.getAttribute("email");
        return ResponseEntity.ok("Authenticated as : " + email);
    }
}
