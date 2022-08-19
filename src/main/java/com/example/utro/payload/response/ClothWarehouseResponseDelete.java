package com.example.utro.payload.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ClothWarehouseResponseDelete {
    HttpStatus httpStatus;
    String message;
}