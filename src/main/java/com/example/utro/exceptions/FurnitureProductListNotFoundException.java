package com.example.utro.exceptions;

public class FurnitureProductListNotFoundException extends RuntimeException{
    public FurnitureProductListNotFoundException(String message) {
        super(message);
    }
}