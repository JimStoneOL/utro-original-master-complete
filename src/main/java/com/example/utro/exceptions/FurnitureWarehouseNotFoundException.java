package com.example.utro.exceptions;

public class FurnitureWarehouseNotFoundException extends RuntimeException{
    public FurnitureWarehouseNotFoundException(String message) {
        super(message);
    }
}