package com.prathamesh.app.auth;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.prathamesh.app.domain.User;
import com.prathamesh.app.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${application.security.jwt.secret-key}")
	private String secretKey;

	@Value("${application.security.jwt.expiration}")
	private int jwtExpirationMs;

	@Autowired
	private UserService userService;

	public String extractUsername(String token) {

		return extractClaim(token, Claims::getSubject);
	}

	public UUID extractUserId(String token) {

		String userId = extractClaim(token, claims -> claims.get("userId", String.class));

		return UUID.fromString(userId);
	}

	public Timestamp extractExpirationAsTimestamp(String token) {

		Date expirationDate = extractClaim(token, Claims::getExpiration);

		return new Timestamp(expirationDate.getTime());
	}

	public String hashWithSHA256(String data) throws Exception {

		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		byte[] hashedBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));

		return Base64.getEncoder().encodeToString(hashedBytes);
	}

	public String generateToken(UserDetails userDetails) {

		if (userDetails instanceof User user) {

			Map<String, Object> userClaims = Map.of("userId", user.getUserId(), "active", user.getActive(), "roles",
					user.getAuthorities());

			return getToken(userDetails.getUsername(), userClaims);
		}

		return null;
	}

	private Claims extractAllClaims(String token) {

		return Jwts.parser().verifyWith(getVerifyKey()).build().parseSignedClaims(token).getPayload();

	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

		final Claims claims = extractAllClaims(token);

		return claimsResolver.apply(claims);
	}

	private Key getSignInKey() {

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);

		return Keys.hmacShaKeyFor(keyBytes);
	}

	private SecretKey getVerifyKey() {

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);

		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String doGenerateRefreshToken(String subject) {

		var user = userService.getByUserName(subject)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		Map<String, Object> userClaims = Map.of("userId", user.getUserId(), "active", user.getActive(), "roles",
				user.getAuthorities());

		return getToken(subject, userClaims);

	}

	private String getToken(String subject, Map<String, Object> userClaims) {

		return Jwts.builder().claims(userClaims).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + jwtExpirationMs)).signWith(getSignInKey()).compact();
	}
}
