package com.nazrin.notex.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.embedding-model}")
    private String embeddingModel;

    @Value("${gemini.embedding-dimensions}")
    private int dimensions;

    private final RestClient restClient = RestClient.create();

    public List<Double> embed(String text, String taskType) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + embeddingModel + ":embedContent";

        Map<String, Object> body = Map.of(
                "model", "models/" + embeddingModel,
                "content", Map.of("parts", List.of(Map.of("text", text))),
                "taskType", taskType,
                "outputDimensionality", dimensions
        );

        Map<?, ?> response = restClient.post()
                .uri(url)
                .header("x-goog-api-key", apiKey)
                .body(body)
                .retrieve()
                .body(Map.class);

        Map<?, ?> embedding = (Map<?, ?>) response.get("embedding");
        List<?> values = (List<?>) embedding.get("values");
        return values.stream().map(v -> ((Number) v).doubleValue()).toList();
    }
}