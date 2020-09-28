package com.zmijewski.ecommerce.exception;

public class ShippingNotFoundException extends RuntimeException {
    public ShippingNotFoundException() {
    }

    public ShippingNotFoundException(String message) {
        super(message);
    }
}
