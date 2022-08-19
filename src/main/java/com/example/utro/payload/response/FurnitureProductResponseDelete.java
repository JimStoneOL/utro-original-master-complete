package com.example.utro.payload.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class FurnitureProductResponseDelete {
    HttpStatus httpStatus;
    String message;
}