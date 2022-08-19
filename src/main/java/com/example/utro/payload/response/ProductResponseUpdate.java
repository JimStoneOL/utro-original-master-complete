package com.example.utro.payload.response;

import com.example.utro.dto.ProductDTO;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ProductResponseUpdate {
    private HttpStatus httpStatus;
    private String message;
    private ProductDTO body;
}