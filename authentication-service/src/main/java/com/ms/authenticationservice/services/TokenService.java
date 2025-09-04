package com.ms.authenticationservice.services;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenService {

    private final SecretKey secretKey;

    public TokenService(@Value("${api.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username){
        return Jwts.builder().subject(username).issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusMillis(1000 * 60 * 60 * 10 )))
                .signWith(secretKey)
                .compact();
    }
}
