package com.example.utro.exceptions;

public class OrdersListNotFoundException extends RuntimeException{
    public OrdersListNotFoundException(String message) {
        super(message);
    }
}