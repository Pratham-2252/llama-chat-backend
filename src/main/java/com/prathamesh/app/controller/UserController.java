package com.prathamesh.app.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

	@Autowired
	private UserService userService;

	@PutMapping(path = "/user-update/{userId}")
	public ResponseEntity<?> update(@PathVariable(value = "userId") UUID userId, @RequestBody UserInfo userInfo) {

		logger.info("Inside update User method");

		userService.update(userId, userInfo);

		logger.info("Finished update User method");

		return ResponseEntity.ok().body("User Updated");
	}
}
