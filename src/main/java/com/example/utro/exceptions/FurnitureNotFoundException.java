package com.example.utro.exceptions;

public class FurnitureNotFoundException extends RuntimeException{
    public FurnitureNotFoundException(String message) {
        super(message);
    }
}