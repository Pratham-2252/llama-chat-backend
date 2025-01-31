package com.prathamesh.app.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prathamesh.app.dto.ErrorResponse;
import com.prathamesh.app.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userDetailsService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getMethod().equals("OPTIONS") || "/ws/info".equals(request.getRequestURI())) {

			return;
		}

		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);

		try {

			username = jwtService.extractUsername(jwt);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authToken);
			}

			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException ex) {

			String isRefreshToken = request.getHeader("isRefreshToken");

			String requestURL = request.getRequestURL().toString();

			if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshToken")) {

				allowForRefreshToken(ex, request);

				filterChain.doFilter(request, response);

			} else {

				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

				ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

				String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

				response.getWriter().write(jsonErrorResponse);
			}
		} catch (Exception ex) {

			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

			ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

			String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

			response.getWriter().write(jsonErrorResponse);
		}

	}

	private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

//		create a UsernamePasswordAuthenticationToken with null values. 

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				null, null, null);

//		After setting the Authentication in the context, we specify that the current
//		user is authenticated. So it passes the Spring Security Configurations successfully.		 

		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

//		Set the claims so that in controller we will be using it to create new JWT

		request.setAttribute("claims", ex.getClaims());
	}

}
