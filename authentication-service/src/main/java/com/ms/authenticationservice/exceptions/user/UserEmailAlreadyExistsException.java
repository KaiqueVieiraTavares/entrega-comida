package com.ms.authenticationservice.exceptions.user;

public class UserEmailAlreadyExistsException extends RuntimeException {
  public UserEmailAlreadyExistsException(String message) {
    super(message);
  }
}
