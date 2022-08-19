package com.example.utro.payload.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class OrderResponseDelete {
    private String message;
    private HttpStatus httpStatus;
}