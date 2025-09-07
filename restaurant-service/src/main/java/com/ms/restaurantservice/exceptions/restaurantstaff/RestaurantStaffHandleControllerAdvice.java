package com.ms.restaurantservice.exceptions.restaurantstaff;

import com.ms.restaurantservice.controllers.RestaurantStaffController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes= RestaurantStaffController.class)
public class RestaurantStaffHandleControllerAdvice {

    @ExceptionHandler(StaffIsAlreadyInRestaurantException.class)
    public ResponseEntity<ProblemDetail> handleStaffIsAlreadyInARestaurant(StaffIsAlreadyInRestaurantException e){
        return buildProblem(HttpStatus.CONFLICT,"The staff is already in a restaurant", e.getMessage());
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFound(UserNotFoundException e){
        return buildProblem(HttpStatus.NOT_FOUND,"User not found", e.getMessage());
    }
    @ExceptionHandler(StaffNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleStaffNotFound(StaffNotFoundException e){
        return buildProblem(HttpStatus.NOT_FOUND,"staff not found", e.getMessage());
    }
    @ExceptionHandler(OwnerCannotExitException.class)
    public ResponseEntity<ProblemDetail> handleOwnerCannotExit(OwnerCannotExitException e){
        return buildProblem(HttpStatus.BAD_REQUEST,"Owner cannot exit from restaurant", e.getMessage());
    }
    @ExceptionHandler(SelfRemovalNotAllowedException.class)
    public ResponseEntity<ProblemDetail> handleSelfRemovalNotAllowed(SelfRemovalNotAllowedException e){
        return buildProblem(HttpStatus.BAD_REQUEST,"Self removal not allowed", e.getMessage());
    }

    private ResponseEntity<ProblemDetail> buildProblem(HttpStatus status, String title, String detail){
        var problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        return ResponseEntity.status(status).body(problem);
    }
}
