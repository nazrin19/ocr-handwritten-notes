package com.nazrin.notex.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    private final RestClient restClient = RestClient.create();

    public String askGemini(String userText) {
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return callGemini(userText);
            } catch (HttpServerErrorException | ResourceAccessException | HttpClientErrorException.TooManyRequests e)  {
                if (attempt == maxAttempts) {
                    throw new ResponseStatusException(
                            HttpStatus.SERVICE_UNAVAILABLE,
                            "The AI service is busy right now. Please try again in a moment.");
                }
                try {
                    Thread.sleep(2000L * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        throw new IllegalStateException("Unreachable");
    }

    private  String callGemini (String userText) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", userText)
                        })
                }
        );

        Map response = restClient.post()
                .uri(url)
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        // Navigate the nested structure step by step
        var candidates = (java.util.List) response.get("candidates");
        var firstCandidate = (Map) candidates.get(0);
        var content = (Map) firstCandidate.get("content");
        var parts = (java.util.List) content.get("parts");
        var firstPart = (Map) parts.get(0);

        return (String) firstPart.get("text");
    }
    public String askGrounded(String notes, String question) {
        String groundedPrompt = """
        Here are some notes:
        ---
        %s
        ---
        Based only on the notes above, answer this question: %s
        If the answer isn't in the notes, say so — don't use outside knowledge.
        """.formatted(notes, question);

        return askGemini(groundedPrompt);
    }

}
