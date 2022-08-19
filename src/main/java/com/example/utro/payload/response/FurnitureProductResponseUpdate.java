package com.example.utro.payload.response;

import com.example.utro.dto.FurnitureProductDTO;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class FurnitureProductResponseUpdate {
    private HttpStatus httpStatus;
    private String message;
    private FurnitureProductDTO body;
}