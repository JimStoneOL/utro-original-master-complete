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
	@Email(message = "Email должен иметь формат почты")
	@NotBlank(message = "Email не должен быть пустым")
	@ValidEmail
	private String email;
	@NotEmpty(message = "Имя не должно быть пустым")
	private String firstname;
	@NotEmpty(message = "Фамилия не должна быть пустым")
	private String lastname;
	@NotEmpty(message = "Пароль не должен быть пустым")
	@Size(min=6)
	private String password;
	private String confirmPassword;

}
