package com.zmijewski.ecommerce.exception;

public class ProductNotInCartException extends RuntimeException {
    public ProductNotInCartException() {
    }

    public ProductNotInCartException(String message) {
        super(message);
    }
}
