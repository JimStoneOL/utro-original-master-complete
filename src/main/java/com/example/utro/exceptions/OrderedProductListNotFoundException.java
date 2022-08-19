package com.example.utro.exceptions;

public class OrderedProductListNotFoundException extends RuntimeException{
    public OrderedProductListNotFoundException(String message) {
        super(message);
    }
}
