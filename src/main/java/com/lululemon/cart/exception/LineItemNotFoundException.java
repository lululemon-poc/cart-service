package com.lululemon.cart.exception;

public class LineItemNotFoundException extends RuntimeException {
    public LineItemNotFoundException(String message) {
        super(message);
    }
}
