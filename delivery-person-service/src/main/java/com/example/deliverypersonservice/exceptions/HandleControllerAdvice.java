package com.example.deliverypersonservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleControllerAdvice {

    @ExceptionHandler(DeliveryPersonNotFound.class)
    public ResponseEntity<ProblemDetail> handleDeliveryPersonNotFound(DeliveryPersonNotFound e){
        var problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setDetail(e.getMessage());
        problem.setTitle("Delivery person not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }
    @ExceptionHandler(VehiclePlateAlreadyExists.class)
    public ResponseEntity<ProblemDetail> handleVehiclePlateAlreadyExists(VehiclePlateAlreadyExists e){
        var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setDetail(e.getMessage());
        problem.setTitle("Delivery plate already exists!");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }
}
