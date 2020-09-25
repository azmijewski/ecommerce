package com.zmijewski.ecommerce.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException() {
    }

    public AddressNotFoundException(String message) {
        super(message);
    }
}
