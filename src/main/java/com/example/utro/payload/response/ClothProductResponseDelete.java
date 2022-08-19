package com.example.utro.payload.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ClothProductResponseDelete {
    HttpStatus httpStatus;
    String message;
}