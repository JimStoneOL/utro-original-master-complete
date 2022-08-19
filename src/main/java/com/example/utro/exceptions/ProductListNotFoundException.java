package com.example.utro.exceptions;

public class ProductListNotFoundException extends RuntimeException{
    public ProductListNotFoundException(String message) {
        super(message);
    }
}