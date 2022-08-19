package com.example.utro.web;


import com.example.utro.security.jwt.JwtUtils;
import com.example.utro.entity.enums.ERole;
import com.example.utro.entity.Role;
import com.example.utro.entity.User;
import com.example.utro.payload.response.JwtResponse;
import com.example.utro.payload.request.LoginRequest;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.payload.request.SignupRequest;
import com.example.utro.repository.RoleRepository;
import com.example.utro.repository.UserRepository;
import com.example.utro.service.UserDetailsImpl;
import com.example.utro.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRespository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	ResponseErrorValidation responseErrorValidation;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authUser(@RequestBody LoginRequest loginRequest) {
		
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(), 
						loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getEmail(), 
				roles));
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {

		ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
		if (!ObjectUtils.isEmpty(errors)) return errors;

		if (userRespository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is exist"));
		}
		
		User user = new User(
				signupRequest.getEmail(),
				passwordEncoder.encode(signupRequest.getPassword()));
		user.setFirstname(signupRequest.getFirstname());
		user.setLastname(signupRequest.getLastname());
		Set<Role> roles = new HashSet<>();
		Role userRole = roleRepository
				.findByName(ERole.ROLE_CUSTOMER)
				.orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
		roles.add(userRole);
		user.setRoles(roles);
		userRespository.save(user);
		return ResponseEntity.ok(new MessageResponse("User CREATED"));
	}
}
