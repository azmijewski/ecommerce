package com.zmijewski.ecommerce.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException() {
    }

    public PaymentNotFoundException(String message) {
        super(message);
    }
}
