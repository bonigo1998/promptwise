package com.promptwise.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PromptAnalysisRequest(

        @NotBlank(message = "Prompt must not be blank")
        @Size(max = 5000, message = "Prompt must not exceed 5000 characters")
        String prompt

) {
}