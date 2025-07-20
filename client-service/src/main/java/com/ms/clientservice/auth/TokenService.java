package com.ms.clientservice.auth;





import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ms.clientservice.entities.ClientEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenService {
    @Value("${api.secret.key}")
    private String key;
    @Value("${api.secret.issuer}")
    private String issuer;

    public String generateToken(ClientEntity clientEntity){
        Algorithm algorithm = Algorithm.HMAC256(key);
        return JWT.create().withIssuer(issuer).withSubject(clientEntity.getEmail())
                .withExpiresAt(Date.from(Instant.now().plus(2, ChronoUnit.HOURS)))
                .sign(algorithm);
    }

    public String verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(key);
            return JWT.require(algorithm).withIssuer(issuer).build().verify(token)
                    .getSubject();
        } catch (JWTVerificationException e){
            throw new RuntimeException("Token invalido ou expirado");
        }
    }
}
