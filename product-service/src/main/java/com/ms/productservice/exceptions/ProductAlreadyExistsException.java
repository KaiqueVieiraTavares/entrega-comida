package com.ms.productservice.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException() {
        super("The name of the product already exists");
    }
}
