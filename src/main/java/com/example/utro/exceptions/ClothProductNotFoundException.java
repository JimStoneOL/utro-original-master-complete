package com.example.utro.exceptions;

public class ClothProductNotFoundException extends RuntimeException{
    public ClothProductNotFoundException(String message) {
        super(message);
    }
}