package com.ms.clientservice.exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionHandlerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleClientNotFoundException(UserNotFoundException e ){
        logger.error("Client not found: {}", e.getMessage());
        var problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Client not found");
        problem.setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ProblemDetail> handleClientNotAuthenticatedException(UserNotAuthenticatedException e){
        logger.error("Client is not authenticated: {}", e.getMessage());
        var problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problem.setTitle("Client is not authenticated");
        problem.setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleEmailAlreadyExistsException(EmailAlreadyExistsException e){
        var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Email already exists");
        problem.setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }
}
