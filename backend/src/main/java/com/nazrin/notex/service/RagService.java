package com.nazrin.notex.service;

import com.nazrin.notex.dto.RagResponse;
import com.nazrin.notex.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {

    private final EmbeddingService embeddingService;
    private final NoteRepository noteRepository;
    private final GeminiService geminiService;

    @Value("${rag.top-k}")
    private int topK;

    public RagService(EmbeddingService embeddingService,
                      NoteRepository noteRepository,
                      GeminiService geminiService) {
        this.embeddingService = embeddingService;
        this.noteRepository = noteRepository;
        this.geminiService = geminiService;
    }

    public RagResponse answer(String question, Long noteId) {
        List<Double> questionVector = embeddingService.embed(question, "RETRIEVAL_QUERY");
        List<String> chunks = noteRepository.findSimilarChunks(questionVector, noteId, topK);

        if (chunks.isEmpty()) {
            return new RagResponse("No notes have been uploaded yet.", chunks);
        }

        String context = String.join("\n\n", chunks);
        String answer = geminiService.askGrounded(context, question);
        return new RagResponse(answer, chunks);
    }
}