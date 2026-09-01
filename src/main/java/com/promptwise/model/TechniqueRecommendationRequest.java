package com.promptwise.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TechniqueRecommendationRequest(

        @NotBlank(message = "Task description must not be blank")
        @Size(
                max = 5000,
                message = "Task description must not exceed 5000 characters"
        )
        String task

) {
}