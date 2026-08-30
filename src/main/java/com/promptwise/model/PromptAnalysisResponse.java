package com.promptwise.model;

import java.util.List;

public record PromptAnalysisResponse(
        String prompt,
        int overallScore,
        int maximumScore,
        String rating,
        List<CriterionResult> criteria
) {
}