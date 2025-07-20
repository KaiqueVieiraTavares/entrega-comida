package com.ms.clientservice.auth;

import com.ms.clientservice.exceptions.ClientNotAuthenticatedException;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@Configuration
public class AuthUtil {

    public static UUID getLoggedUserId(){
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth==null ||!auth.isAuthenticated()){
            throw new ClientNotAuthenticatedException("Client is not authenticated");
        }
        return UUID.fromString(auth.getName());
    }
}
