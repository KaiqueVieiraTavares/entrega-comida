package com.ms.productservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class HandleControllerAdvice {

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ProblemDetail handleProductAlreadyExistsException(ProductAlreadyExistsException e){
        return buildProblem("Product already exists", e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handlerProductNotFoundException(ProductNotFoundException e){

        return buildProblem("Product not found", e.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ProblemDetail handleUnauthorizedAccessException(UnauthorizedAccessException e){
        return buildProblem("not Authorized to this content", e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InsufficientStockException.class)
    public ProblemDetail handleInsufficientStock(InsufficientStockException e){
        return buildProblem("Insufficient stock!", e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    private ProblemDetail buildProblem(String title, String message, HttpStatus httpStatus){
        var problem = ProblemDetail.forStatusAndDetail(httpStatus, message);
        problem.setTitle(title);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
