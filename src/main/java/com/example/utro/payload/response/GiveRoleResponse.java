package com.example.utro.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiveRoleResponse {
    private UserRoleResponse body;
    private String message;
    private HttpStatus httpStatus;
}
