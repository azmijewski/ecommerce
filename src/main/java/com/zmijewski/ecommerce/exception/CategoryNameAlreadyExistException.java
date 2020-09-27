package com.zmijewski.ecommerce.exception;

public class CategoryNameAlreadyExistException extends RuntimeException{
    public CategoryNameAlreadyExistException() {
    }

    public CategoryNameAlreadyExistException(String message) {
        super(message);
    }
}
