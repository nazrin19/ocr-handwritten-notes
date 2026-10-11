package com.nazrin.notex.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateNoteRequest(
        @NotBlank(message = "title must not be empty") String title,
        @NotBlank(message = "text must not be empty") String text
) {
}