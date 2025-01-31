package com.prathamesh.app.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
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

	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String googleClientId;

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

	public AuthenticationResponse authenticateOAuth2(String credential) {

		try {

			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
					new GsonFactory()).setAudience(Collections.singletonList(googleClientId)).build();

			GoogleIdToken idToken = verifier.verify(credential);

			if (idToken != null) {

				GoogleIdToken.Payload payload = idToken.getPayload();

				String email = payload.getEmail();

				var user = userService.getByUserName(email).orElseGet(() -> {

					String name = (String) payload.get("name");

					UserInfo userInfo = new UserInfo();

					userInfo.setFirstName(name);
					userInfo.setLastName("-");
					userInfo.setUserName(email);
					userInfo.setEmail(email);
					userInfo.setPassword("-");
					userInfo.setRole("Admin");

					return userService.save(userInfo);
				});

				var jwtToken = jwtService.generateToken(user);

				AuthenticationResponse response = new AuthenticationResponse(jwtToken);

				return response;

			} else {

				throw new RuntimeException("Invalid ID token.");
			}

		} catch (GeneralSecurityException | IOException e) {

			throw new RuntimeException("Error verifying ID token.", e);
		}
	}
}
