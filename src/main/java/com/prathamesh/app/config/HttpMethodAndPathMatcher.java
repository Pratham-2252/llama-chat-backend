package com.prathamesh.app.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.http.HttpServletRequest;

public class HttpMethodAndPathMatcher implements RequestMatcher {

	private final HttpMethod httpMethod;
	private final AntPathRequestMatcher antPathRequestMatcher;

	public HttpMethodAndPathMatcher(HttpMethod method, String pattern) {

		this.httpMethod = method;
		this.antPathRequestMatcher = new AntPathRequestMatcher(pattern);
	}

	@Override
	public boolean matches(HttpServletRequest request) {

		return httpMethod.matches(request.getMethod()) && antPathRequestMatcher.matches(request);
	}

}
