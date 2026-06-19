package com.ms.authenticationservice.exceptions.user;


import com.ms.authenticationservice.controllers.auth.AuthController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice(basePackageClasses = AuthController.class)
public class HandleControllerAdvice {


    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException e){
       return buildProblem("Bad credencials", e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExists(UserEmailAlreadyExistsException e){
       return buildProblem("Email already exists", e.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException e){
        return buildProblem("User not found", e.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ProblemDetail buildProblem(String title, String detail, HttpStatus httpStatus){
        var problem = ProblemDetail.forStatusAndDetail(httpStatus, detail);
        problem.setTitle(title);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
