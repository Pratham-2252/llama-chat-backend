package com.prathamesh.app.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

	private final ObjectMapper objectMapper;

	public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		Map<String, Object> errorDetails = new HashMap<>();

		if (authException instanceof InsufficientAuthenticationException) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			errorDetails.put("error", "Bad credentials");
			errorDetails.put("message", "Access denied due to missing or invalid credentials");

		} else {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			errorDetails.put("error", "Unauthorized");
			errorDetails.put("message", "Access denied due to missing or invalid credentials");
		}

		logger.info("Access denied due to missing or invalid credentials");

		response.setContentType("application/json;charset=UTF-8");

		PrintWriter writer = response.getWriter();

		writer.write(objectMapper.writeValueAsString(errorDetails));
		writer.flush();
	}

}
