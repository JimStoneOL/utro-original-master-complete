package com.example.utro.payload.response;

import com.example.utro.dto.ClothWarehouseDTO;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ClothWarehouseResponseUpdate {
    private HttpStatus httpStatus;
    private String message;
    private ClothWarehouseDTO body;
}