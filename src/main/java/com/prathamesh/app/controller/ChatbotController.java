package com.prathamesh.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prathamesh.app.service.OlammaService;

@RestController
@RequestMapping("/api/v1")
public class ChatbotController {

	@Autowired
	private OlammaService olammaService;

	@PostMapping(path = "/chatbot/ask", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> askChatbot(@RequestParam String prompt) {

		String model = "llama3.2";

		String chatbotResponse = olammaService.getChatbotResponse(model, prompt);

		return ResponseEntity.ok(chatbotResponse);
	}
}
