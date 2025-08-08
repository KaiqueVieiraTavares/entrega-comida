package com.ms.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.io.Decoders;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.List;



@Component
public class GatewayFilter implements GlobalFilter, Ordered {
    private final SecretKey keySecret;

    public GatewayFilter(@Value("${api.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secret);
        this.keySecret = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> openUrls = List.of();
        String path = exchange.getRequest().getURI().getPath();

        if(openUrls.stream().anyMatch(path::startsWith)){
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if(authHeader == null || !(authHeader.startsWith("Bearer "))){
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.replace("Bearer ", "");
        try{
            JwtParser jwtParser = Jwts.parser().verifyWith(keySecret).build();
            Jws<Claims> jws = jwtParser.parseSignedClaims(token);
            Claims claims = jws.getPayload();

            String userId = claims.get("userId", String.class);
            String role = claims.get("role", String.class);

            ServerHttpRequest mutatedHttpRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId!=null? userId : "")
                    .header("X-User-Role", role!=null ? role : "")
                    .build();

            ServerWebExchange mutatedWebExchange = exchange.mutate().request(mutatedHttpRequest).build();
            return chain.filter(mutatedWebExchange);
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
