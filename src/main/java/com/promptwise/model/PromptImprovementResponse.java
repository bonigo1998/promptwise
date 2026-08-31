package com.promptwise.model;

import java.util.List;

public record PromptImprovementResponse(
        String originalPrompt,
        String improvedPrompt,
        List<String> improvements
) {
}