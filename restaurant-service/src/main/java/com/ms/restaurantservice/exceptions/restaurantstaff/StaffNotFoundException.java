package com.ms.restaurantservice.exceptions.restaurantstaff;

public class StaffNotFoundException extends RuntimeException {
  public StaffNotFoundException(String message) {
    super(message);
  }
}
