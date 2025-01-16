package com.prathamesh.app.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OlammaService {

	private static final String OLAMMA_URL = "http://127.0.0.1:11434/completions";

	public String getChatbotResponse(String model, String prompt) {

		try {

			RestTemplate restTemplate = new RestTemplate();

			String requestBody = new ObjectMapper().writeValueAsString(Map.of("model", model, "prompt", prompt));

			HttpHeaders headers = new HttpHeaders();

			headers.add("Content-Type", "application/json");

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(OLAMMA_URL, entity, String.class);

			JsonNode jsonResponse = new ObjectMapper().readTree(response.getBody());

			return jsonResponse.path("completion").asText();

		} catch (JsonProcessingException e) {

			return "Error: Unable to fetch response from Olamma.";
		}
	}

}
