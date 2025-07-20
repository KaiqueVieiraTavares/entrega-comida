package com.ms.clientservice.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;

    public SecurityFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.extractToken(request);
        if(token!=null){
            try{
                String subject = tokenService.verifyToken(token);
                var auth = new UsernamePasswordAuthenticationToken(subject, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e ){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        }
        filterChain.doFilter(request, response);
    }
    private String extractToken(HttpServletRequest request){
       String header =  request.getHeader("Authorization");

       if(header!=null){
           return header.replace("Bearer ", "");
       }
       return null;
    }
}
