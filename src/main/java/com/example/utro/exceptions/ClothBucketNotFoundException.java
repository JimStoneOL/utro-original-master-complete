package com.example.utro.exceptions;

public class ClothBucketNotFoundException extends RuntimeException{
    public ClothBucketNotFoundException(String message) {
        super(message);
    }
}
