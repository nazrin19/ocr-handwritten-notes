package com.nazrin.notex.service;

import com.nazrin.notex.dto.CreateNoteResponse;
import com.nazrin.notex.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {

    private final ChunkingService chunkingService;
    private final EmbeddingService embeddingService;
    private final NoteRepository noteRepository;

    public NoteService(ChunkingService chunkingService,
                       EmbeddingService embeddingService,
                       NoteRepository noteRepository) {
        this.chunkingService = chunkingService;
        this.embeddingService = embeddingService;
        this.noteRepository = noteRepository;
    }

    public CreateNoteResponse createNote(String title, String text) {
        List<String> chunks = chunkingService.chunk(text);

        List<List<Double>> vectors = new ArrayList<>();
        for (String chunk : chunks) {
            vectors.add(embeddingService.embed(chunk, "RETRIEVAL_DOCUMENT"));
        }

        long noteId = noteRepository.saveNote(title, chunks, vectors);
        return new CreateNoteResponse(noteId, chunks.size());
    }
}