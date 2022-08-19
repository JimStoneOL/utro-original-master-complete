package com.example.utro.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
	@NotEmpty(message = "login cannot be empty")
	private String username;
	@NotEmpty(message = "Password cannot be empty")
	private String password;
}
