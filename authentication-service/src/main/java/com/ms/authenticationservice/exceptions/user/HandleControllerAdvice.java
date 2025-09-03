package com.ms.authenticationservice.exceptions.user;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class HandleControllerAdvice {


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentials(BadCredentialsException e){
        var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Invalid credentials");
        problem.setDetail("The username or password provided is incorrect.");
        return ResponseEntity.of(Optional.of(problem));
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleEmailAlreadyExists(UserEmailAlreadyExistsException e){
        var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setDetail("Email already exists");
        problem.setTitle(e.getMessage());
        return ResponseEntity.of(Optional.of(problem));
    }
}
