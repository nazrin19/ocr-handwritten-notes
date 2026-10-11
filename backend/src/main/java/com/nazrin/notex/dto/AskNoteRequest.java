package com.nazrin.notex.dto;

import jakarta.validation.constraints.NotBlank;

public record AskNoteRequest(
        Long noteId,
        @NotBlank(message = "question must not be empty") String question
) {
}
