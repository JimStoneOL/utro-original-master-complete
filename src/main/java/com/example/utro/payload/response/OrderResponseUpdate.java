package com.example.utro.payload.response;

import com.example.utro.dto.OrderDTO;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class OrderResponseUpdate {
    private HttpStatus httpStatus;
    private String message;
    private OrderDTO body;
}
