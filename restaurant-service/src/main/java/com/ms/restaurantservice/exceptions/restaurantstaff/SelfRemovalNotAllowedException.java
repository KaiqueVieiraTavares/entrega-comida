package com.ms.restaurantservice.exceptions.restaurantstaff;

public class SelfRemovalNotAllowedException extends RuntimeException {
    public SelfRemovalNotAllowedException(String message) {
        super(message);
    }
}
