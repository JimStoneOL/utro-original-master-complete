package com.example.utro.payload.response;

import com.example.utro.dto.ClothProductDTO;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ClothProductResponseUpdate {
    private HttpStatus httpStatus;
    private String message;
    private ClothProductDTO body;
}