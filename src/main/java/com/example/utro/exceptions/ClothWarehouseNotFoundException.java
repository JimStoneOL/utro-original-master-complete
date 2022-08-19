package com.example.utro.exceptions;

public class ClothWarehouseNotFoundException extends RuntimeException{
    public ClothWarehouseNotFoundException(String message) {
        super(message);
    }
}