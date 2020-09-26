package com.zmijewski.ecommerce.exception;

public class PaymentExecutionException extends RuntimeException {
    public PaymentExecutionException() {
    }

    public PaymentExecutionException(String message) {
        super(message);
    }

    public PaymentExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
