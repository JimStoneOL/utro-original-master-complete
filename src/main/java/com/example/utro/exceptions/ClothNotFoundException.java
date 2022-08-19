package com.example.utro.exceptions;

public class ClothNotFoundException extends RuntimeException{
    public ClothNotFoundException(String message) {
        super(message);
    }
}