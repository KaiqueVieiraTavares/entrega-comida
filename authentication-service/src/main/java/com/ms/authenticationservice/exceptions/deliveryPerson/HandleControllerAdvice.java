package com.ms.authenticationservice.exceptions.deliveryPerson;

import com.example.deliverypersonservice.controllers.DeliveryPersonController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = DeliveryPersonController.class)
public class HandleControllerAdvice {

    @ExceptionHandler(VehiclePlateAlreadyExists.class)
    public ResponseEntity<ProblemDetail> handleVehiclePlateAlreadyExists(VehiclePlateAlreadyExists e){
        var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Vehicle plate already exists");
        problem.setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(DeliveryPersonEmailAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleEmailAlreadyExists(DeliveryPersonEmailAlreadyExistsException e){
        var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Email already exists");
        problem.setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }
}
