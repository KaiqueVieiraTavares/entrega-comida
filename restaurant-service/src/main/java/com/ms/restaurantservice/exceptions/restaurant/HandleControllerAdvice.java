package com.ms.restaurantservice.exceptions.restaurant;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static org.springframework.http.ResponseEntity.*;

@RestControllerAdvice
public class HandleControllerAdvice {

    @ExceptionHandler(RestaurantAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleRestaurantAlreadyExistsException(RestaurantAlreadyExistsException e){
        var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Restaurant name already exists");
        problem.setDetail(e.getMessage());
        return status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(RestaurantAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleRestaurantNotFoundException(RestaurantNotFoundException e){
        var problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Restaurant not found");
        problem.setDetail(e.getMessage());
        return status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorizedAccessException(UnauthorizedAccessException e){
        var problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problem.setDetail("Not authorized to this content");
        problem.setDetail(e.getMessage());
        return status(HttpStatus.UNAUTHORIZED).body(problem);
    }
}
