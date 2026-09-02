package com.promptwise.model;

import java.util.List;

public record ResponsibleAiAssessment(
        String dimension,
        String riskLevel,
        int score,
        List<String> findings,
        List<String> recommendations
) {
}