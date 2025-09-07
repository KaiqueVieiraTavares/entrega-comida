package com.ms.restaurantservice.exceptions.restaurantstaff;

public class InvalidStaffRoleException extends RuntimeException {
    public InvalidStaffRoleException(String message) {
        super(message);
    }
}
