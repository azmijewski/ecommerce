package com.zmijewski.ecommerce.exception;

public class PaymentCreationException extends RuntimeException {
    public PaymentCreationException() {
    }

    public PaymentCreationException(String message) {
        super(message);
    }

    public PaymentCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
