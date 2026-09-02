package com.promptwise.model;

import java.util.List;

public record ResponsibleAiCheckResponse(
        String scenario,
        String overallRiskLevel,
        int overallRiskScore,
        List<ResponsibleAiAssessment> assessments,
        String disclaimer
) {
}