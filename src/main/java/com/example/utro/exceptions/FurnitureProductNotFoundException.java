package com.example.utro.exceptions;

public class FurnitureProductNotFoundException extends RuntimeException{
    public FurnitureProductNotFoundException(String message) {
        super(message);
    }
}