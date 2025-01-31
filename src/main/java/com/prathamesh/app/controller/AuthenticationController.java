package com.prathamesh.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prathamesh.app.dto.AuthenticationRequest;
import com.prathamesh.app.dto.AuthenticationResponse;
import com.prathamesh.app.dto.ErrorResponse;
import com.prathamesh.app.dto.UserInfo;
import com.prathamesh.app.exceptions.UserAlreadyExistsException;
import com.prathamesh.app.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/auth/register")
	public ResponseEntity<?> register(@RequestBody UserInfo userInfo) {

		try {

			authenticationService.register(userInfo);

			return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");

		} catch (UserAlreadyExistsException ex) {

			return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
		}
	}

	@PostMapping("/auth/authenticate")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {

		try {

			AuthenticationResponse authenticate = authenticationService.authenticate(authenticationRequest);

			return ResponseEntity.ok(authenticate);

		} catch (BadCredentialsException ex) {

			ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
		}
	}
}
