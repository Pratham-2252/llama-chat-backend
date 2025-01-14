package com.prathamesh.app.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 9100610295507934104L;

	public UserAlreadyExistsException(String message) {
		super(message);
	}
}
