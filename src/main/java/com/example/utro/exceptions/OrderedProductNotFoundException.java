package com.example.utro.exceptions;

public class OrderedProductNotFoundException extends RuntimeException{
    public OrderedProductNotFoundException(String message) {
        super(message);
    }
}
