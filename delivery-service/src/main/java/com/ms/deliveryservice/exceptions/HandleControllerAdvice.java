package com.ms.deliveryservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class HandleControllerAdvice {

    @ExceptionHandler(DeliveryNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleDeliveryNotFound(DeliveryNotFoundException e){
        var problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Delivery not found!");
        problem.setDetail(e.getMessage());
        return ResponseEntity.of(Optional.of(problem));
    }
}
