package com.promptwise.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BiasClassificationRequest(

        @NotBlank(message = "Scenario must not be blank")
        @Size(
                max = 5000,
                message = "Scenario must not exceed 5000 characters"
        )
        String scenario

) {
}