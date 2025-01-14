package com.prathamesh.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.prathamesh.app.auth.JwtService;
import com.prathamesh.app.dto.AuthenticationRequest;
import com.prathamesh.app.dto.AuthenticationResponse;
import com.prathamesh.app.dto.UserInfo;

@Service
public class AuthenticationService {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;

	public void register(UserInfo userInfo) {

		userService.save(userInfo);
	}

	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

		try {

			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUserName(), authenticationRequest.getPassword());

			authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		} catch (BadCredentialsException ex) {

			throw new BadCredentialsException(ex.getMessage());
		}

		var user = userService.getByUserName(authenticationRequest.getUserName())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		if (!authenticationRequest.getUserName().equals(user.getUsername())) {

			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}

		var jwtToken = jwtService.generateToken(user);

		AuthenticationResponse response = new AuthenticationResponse(jwtToken);

		return response;
	}
}
