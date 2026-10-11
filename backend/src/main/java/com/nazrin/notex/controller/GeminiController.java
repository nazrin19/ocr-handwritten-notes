package com.nazrin.notex.controller;

import com.nazrin.notex.dto.AskRequest;
import com.nazrin.notex.dto.AskResponse;
import com.nazrin.notex.service.ChunkingService;
import com.nazrin.notex.service.EmbeddingService;
import com.nazrin.notex.service.GeminiService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.nazrin.notex.util.VectorMath;

import java.util.List;
import java.util.Map;

@RestController
public class GeminiController {

    private final GeminiService geminiService;
    private final ChunkingService chunkingService;
    private final EmbeddingService embeddingService;

    public GeminiController(GeminiService geminiService,
                            ChunkingService chunkingService,
                            EmbeddingService embeddingService) {
        this.geminiService = geminiService;
        this.chunkingService = chunkingService;
        this.embeddingService = embeddingService;
    }

    @PostMapping("/ask")
    public AskResponse ask(@Valid @RequestBody AskRequest request) {
        String answer = geminiService.askGrounded(request.notes(), request.question());
        return new AskResponse(answer);
    }

  
}