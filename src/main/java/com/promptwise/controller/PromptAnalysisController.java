package com.promptwise.controller;

import com.promptwise.model.PromptAnalysisRequest;
import com.promptwise.model.PromptAnalysisResponse;
import com.promptwise.service.PromptAnalysisService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prompts")
public class PromptAnalysisController {

    private final PromptAnalysisService promptAnalysisService;

    public PromptAnalysisController(
            PromptAnalysisService promptAnalysisService
    ) {
        this.promptAnalysisService = promptAnalysisService;
    }

    @PostMapping("/analyze")
    public PromptAnalysisResponse analyze(
            @Valid @RequestBody PromptAnalysisRequest request
    ) {
        return promptAnalysisService.analyze(request.prompt());
    }
}