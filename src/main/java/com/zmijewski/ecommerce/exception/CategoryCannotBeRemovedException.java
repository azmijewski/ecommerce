package com.zmijewski.ecommerce.exception;

public class CategoryCannotBeRemovedException extends RuntimeException {
    public CategoryCannotBeRemovedException() {
    }

    public CategoryCannotBeRemovedException(String message) {
        super(message);
    }
}
