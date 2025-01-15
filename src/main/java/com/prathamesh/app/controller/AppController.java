package com.prathamesh.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AppController {

	private Logger logger = LoggerFactory.getLogger(AppController.class);

	@GetMapping(path = "/admin/app-test", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> appData() {

		logger.info("Inside App Controller.");

		logger.info("Finished App Controller.");

		return ResponseEntity.ok().body("This is App Application of Prathamesh Sonawane.");
	}
}
