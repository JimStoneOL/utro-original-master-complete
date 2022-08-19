package com.example.utro.payload.response;

import com.example.utro.dto.FurnitureWarehouseDTO;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class FurnitureWarehouseResponseUpdate {
    private HttpStatus httpStatus;
    private String message;
    private FurnitureWarehouseDTO body;
}