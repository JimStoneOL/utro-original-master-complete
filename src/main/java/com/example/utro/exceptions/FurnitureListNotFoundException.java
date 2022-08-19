package com.example.utro.exceptions;

public class FurnitureListNotFoundException extends RuntimeException{
    public FurnitureListNotFoundException(String message) {
        super(message);
    }
}