package com.example.utro.exceptions;

public class ClothProductListNotFoundException extends RuntimeException{
    public ClothProductListNotFoundException(String message) {
        super(message);
    }
}