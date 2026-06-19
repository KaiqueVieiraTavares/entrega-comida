package com.ms.authenticationservice.exceptions.deliveryPerson;

import com.ms.authenticationservice.controllers.delivery_person.DeliverypersonController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice(basePackageClasses = DeliverypersonController.class)
public class HandleControllerAdvice {


    @ExceptionHandler(VehiclePlateAlreadyExists.class)
    public ProblemDetail handleVehiclePlateAlreadyExists(VehiclePlateAlreadyExists e){
        return buildProblem("Vehicle plate already exists", e.getMessage(), HttpStatus.CONFLICT);
    }

    private ProblemDetail buildProblem(String title, String detail, HttpStatus httpStatus){
        var problem = ProblemDetail.forStatusAndDetail(httpStatus, detail);
        problem.setTitle(title);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
