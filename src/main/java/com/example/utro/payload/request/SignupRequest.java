package com.example.utro.payload.request;

import com.example.utro.annotations.PasswordMatches;
import com.example.utro.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@PasswordMatches
public class SignupRequest {
	@Email(message = "It should have email format")
	@NotBlank(message = "User email is required")
	@ValidEmail
	private String email;
	@NotEmpty(message = "Please enter your name")
	private String firstname;
	@NotEmpty(message = "Please enter your lastname")
	private String lastname;
	@NotEmpty(message = "Password is required")
	@Size(min=6)
	private String password;
	private String confirmPassword;

}
