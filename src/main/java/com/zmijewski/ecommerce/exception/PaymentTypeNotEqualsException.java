package com.zmijewski.ecommerce.exception;

public class PaymentTypeNotEqualsException extends RuntimeException {
    public PaymentTypeNotEqualsException() {
    }

    public PaymentTypeNotEqualsException(String message) {
        super(message);
    }
}
