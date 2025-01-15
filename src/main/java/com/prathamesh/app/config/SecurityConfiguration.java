package com.prathamesh.app.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

import com.prathamesh.app.auth.JwtAuthenticationEntryPoint;
import com.prathamesh.app.auth.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private AuthenticationProvider authenticationProvider;
	@Autowired
	private JwtAuthenticationFilter jwtAuthFilter;
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired
	private CorsConfigurationSource corsConfigurationSource;

	private List<String> adminGetEndPoints = List.of("/api/v1/admin/app-test");

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http

				.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((authorizeHttpRequests) -> {
					authorizeHttpRequests.requestMatchers("/api/v1/auth/**").permitAll();
				}).authorizeHttpRequests((authorizeHttpRequests) -> {
					authorizeHttpRequests.requestMatchers("/").hasRole("User");
				}).authorizeHttpRequests((authorizeHttpRequests) -> {
					authorizeHttpRequests
							.requestMatchers(createHttpMethodAndPathMatchers(HttpMethod.GET, adminGetEndPoints))
							.hasRole("Admin");
				}).authorizeHttpRequests((authorizeHttpRequests) -> {
					authorizeHttpRequests.anyRequest().authenticated();
				}).exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
					httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint);
				})
//				.logout(logout -> logout.logoutUrl("/api/v1/auth/logout").addLogoutHandler(customLogoutHandler)
//						.invalidateHttpSession(true).logoutSuccessHandler(
//								(request, response, authentication) -> SecurityContextHolder.clearContext()))
				.sessionManagement(
						(sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)

				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.cors(cors -> cors.configurationSource(corsConfigurationSource));

		return http.build();
	}

	RequestMatcher[] createRequestMatchers(List<String> endPoints) {

		return endPoints.stream().map(AntPathRequestMatcher::new).toArray(RequestMatcher[]::new);
	}

	private RequestMatcher[] createHttpMethodAndPathMatchers(HttpMethod method, List<String> endPoints) {

		return endPoints.stream().map(endpoint -> new HttpMethodAndPathMatcher(method, endpoint))
				.toArray(RequestMatcher[]::new);
	}
}
