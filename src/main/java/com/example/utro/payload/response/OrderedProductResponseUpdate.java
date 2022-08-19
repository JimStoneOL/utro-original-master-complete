package com.example.utro.payload.response;

import com.example.utro.dto.OrderedProductDTO;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class OrderedProductResponseUpdate {
    private HttpStatus httpStatus;
    private String message;
    private OrderedProductDTO body;
}
