package com.prathamesh.app.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomLogoutHandler implements LogoutHandler {

	private Logger logger = LoggerFactory.getLogger(CustomLogoutHandler.class);

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

		logger.info("Inside logout method.");
		logger.info("Finished logout method.");
	}

}
