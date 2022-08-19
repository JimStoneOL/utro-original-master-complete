package com.example.utro.security.jwt;

import com.example.utro.security.SecurityConstants;
import com.example.utro.payload.response.InvalidLoginResponse;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		InvalidLoginResponse loginResponse=new InvalidLoginResponse();
		String jsonLoginResponse=new Gson().toJson(loginResponse);
		response.setContentType(SecurityConstants.CONTENT_TYPE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.getWriter().println(jsonLoginResponse);
	}

}