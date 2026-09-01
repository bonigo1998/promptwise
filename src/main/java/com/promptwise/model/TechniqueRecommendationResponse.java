package com.promptwise.model;

import java.util.List;

public record TechniqueRecommendationResponse(
        String task,
        String recommendedTechnique,
        String explanation,
        List<String> alternatives,
        List<String> detectedSignals
) {
}