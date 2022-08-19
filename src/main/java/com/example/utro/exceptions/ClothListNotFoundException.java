package com.example.utro.exceptions;

public class ClothListNotFoundException extends RuntimeException{
    public ClothListNotFoundException(String message) {
        super(message);
    }
}