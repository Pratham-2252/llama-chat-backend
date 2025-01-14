package com.prathamesh.app.dto;

public class AuthenticationResponse {

	private final String accessToken;

	public AuthenticationResponse(String accessToken) {
		this.accessToken = accessToken;

	}

	public String getAccessToken() {
		return accessToken;
	}
}
