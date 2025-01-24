package com.prathamesh.app.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prathamesh.app.dto.AuthenticationRequest;
import com.prathamesh.app.service.EncryptionService;

@RestController
@RequestMapping("/api/v1")
public class AppController {

	private Logger logger = LoggerFactory.getLogger(AppController.class);

	@Autowired
	private EncryptionService encryptionService;

	@GetMapping(path = "/admin/app-test", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> appData() {

		logger.info("Inside App Controller.");

		logger.info("Finished App Controller.");

		return ResponseEntity.ok().body("This is App Application of Prathamesh Sonawane.");
	}

	@PostMapping("/test")
	public ResponseEntity<?> handleEncryptedData(@RequestBody Map<String, String> requestBody) {

		try {

			String encryptedPayload = requestBody.get("payload");

			String decryptedData = encryptionService.decryptData(encryptedPayload);

			ObjectMapper objectMapper = new ObjectMapper();

			AuthenticationRequest authenticationRequest = objectMapper.readValue(decryptedData,
					AuthenticationRequest.class);

			return ResponseEntity.ok().body(authenticationRequest);

		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error decrypting data: " + e.getMessage());
		}
	}
}
