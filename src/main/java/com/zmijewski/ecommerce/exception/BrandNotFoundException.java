package com.zmijewski.ecommerce.exception;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException() {
    }

    public BrandNotFoundException(String message) {
        super(message);
    }
}
