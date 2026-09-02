package com.promptwise.controller;

import com.promptwise.model.ApiOverviewResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiOverviewController {

    @GetMapping
    public ApiOverviewResponse overview() {
        Map<String, String> endpoints =
                new LinkedHashMap<>();

        endpoints.put(
                "health",
                "GET /api/health"
        );

        endpoints.put(
                "promptAnalyzer",
                "POST /api/prompts/analyze"
        );

        endpoints.put(
                "promptImprovement",
                "POST /api/prompts/improve"
        );

        endpoints.put(
                "techniqueRecommender",
                "POST /api/techniques/recommend"
        );

        endpoints.put(
                "biasClassifier",
                "POST /api/bias/classify"
        );

        endpoints.put(
                "responsibleAiChecker",
                "POST /api/responsible-ai/check"
        );

        return new ApiOverviewResponse(
                "PromptWise",
                "0.0.1",
                "UP",
                endpoints
        );
    }
}