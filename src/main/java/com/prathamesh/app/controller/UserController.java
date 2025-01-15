package com.prathamesh.app.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prathamesh.app.dto.UserInfo;
import com.prathamesh.app.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);

	private UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@PutMapping(path = "/admin/user-update/{userId}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> update(@PathVariable(value = "userId") UUID userId, @RequestBody UserInfo userInfo) {

		logger.info("Inside update User method");

		userService.update(userId, userInfo);

		logger.info("Finished update User method");

		return ResponseEntity.ok().body("User Updated");
	}

	@GetMapping(value = "/user/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> getUserByUserId(@PathVariable UUID userId) {

		logger.info("Retrieving user by ID: {}", userId);

		UserInfo userInfo = userService.getUserByUserId(userId);
		
		logger.info("Finished getUserByUserId method");

		return ResponseEntity.ok(userInfo);
	}
}
