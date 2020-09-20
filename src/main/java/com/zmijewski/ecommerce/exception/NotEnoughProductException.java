package com.zmijewski.ecommerce.exception;

public class NotEnoughProductException extends RuntimeException {
    public NotEnoughProductException() {
    }

    public NotEnoughProductException(String message) {
        super(message);
    }
}
