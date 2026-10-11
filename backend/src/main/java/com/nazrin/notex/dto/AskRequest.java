package com.nazrin.notex.dto;

import jakarta.validation.constraints.NotBlank;

public record AskRequest(
        @NotBlank(message = "notes must not be empty") String notes,
        @NotBlank(message = "question must not be empty") String question
) {
}
