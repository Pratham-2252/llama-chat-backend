package com.prathamesh.app.dto;

public class RegisterUserResponse {

	String message;

	public RegisterUserResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
