package com.example.utro.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
}