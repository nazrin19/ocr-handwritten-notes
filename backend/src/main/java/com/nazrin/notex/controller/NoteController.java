package com.nazrin.notex.controller;

import com.nazrin.notex.dto.AskNoteRequest;
import com.nazrin.notex.dto.CreateNoteRequest;
import com.nazrin.notex.dto.CreateNoteResponse;
import com.nazrin.notex.dto.RagResponse;
import com.nazrin.notex.service.NoteService;
import com.nazrin.notex.service.RagService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final RagService ragService;

    public NoteController(NoteService noteService, RagService ragService) {
        this.noteService = noteService;
        this.ragService = ragService;
    }

    @PostMapping
    public CreateNoteResponse create(@Valid @RequestBody CreateNoteRequest request) {
        return noteService.createNote(request.title(), request.text());
    }

    @PostMapping("/ask")
    public RagResponse ask(@Valid @RequestBody AskNoteRequest request) {
        return ragService.answer(request.question(), request.noteId());
    }
}