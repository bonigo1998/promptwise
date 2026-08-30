package com.promptwise.model;

public record CriterionResult(
        String criterion,
        int score,
        int maximumScore,
        String suggestion
) {
}