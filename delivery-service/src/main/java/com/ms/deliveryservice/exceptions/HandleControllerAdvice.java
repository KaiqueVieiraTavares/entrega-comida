package com.ms.deliveryservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class HandleControllerAdvice {

    @ExceptionHandler(DeliveryNotFoundException.class)
    public ProblemDetail handleDeliveryNotFound(DeliveryNotFoundException e){
        return buildProblem("Delivery not found!", e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DeliveryBusinessException.class)
    public ProblemDetail handleDeliveryBusiness(DeliveryBusinessException e){
        return buildProblem("business rule error", e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    private ProblemDetail buildProblem(String title, String message, HttpStatus httpStatus){
        var problem = ProblemDetail.forStatusAndDetail(httpStatus, message);
        problem.setTitle(title);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
