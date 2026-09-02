package com.promptwise.model;

import java.util.List;

public record BiasClassificationResponse(
        String scenario,
        String biasType,
        int confidence,
        String explanation,
        List<String> detectedIndicators
) {
}