package com.example.utro.payload.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ProductResponseDelete {
    HttpStatus httpStatus;
    String message;
}