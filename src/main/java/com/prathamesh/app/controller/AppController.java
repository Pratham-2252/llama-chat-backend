package com.prathamesh.app.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AppController {

	@GetMapping(path = "/app-test", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<?> appData() {

		return ResponseEntity.ok().body("This is App Application.");
	}
}
