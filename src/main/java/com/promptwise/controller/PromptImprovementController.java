package com.promptwise.controller;

import com.promptwise.model.PromptImprovementRequest;
import com.promptwise.model.PromptImprovementResponse;
import com.promptwise.service.PromptImprovementService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prompts")
public class PromptImprovementController {

    private final PromptImprovementService promptImprovementService;

    public PromptImprovementController(
            PromptImprovementService promptImprovementService
    ) {
        this.promptImprovementService = promptImprovementService;
    }

    @PostMapping("/improve")
    public PromptImprovementResponse improve(
            @Valid @RequestBody PromptImprovementRequest request
    ) {
        return promptImprovementService.improve(request.prompt());
    }
}