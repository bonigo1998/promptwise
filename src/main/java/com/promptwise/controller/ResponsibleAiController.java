package com.promptwise.controller;

import com.promptwise.model.ResponsibleAiCheckRequest;
import com.promptwise.model.ResponsibleAiCheckResponse;
import com.promptwise.service.ResponsibleAiService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/responsible-ai")
public class ResponsibleAiController {

    private final ResponsibleAiService service;

    public ResponsibleAiController(
            ResponsibleAiService service
    ) {
        this.service = service;
    }

    @PostMapping("/check")
    public ResponsibleAiCheckResponse check(
            @Valid @RequestBody
            ResponsibleAiCheckRequest request
    ) {
        return service.check(request.scenario());
    }
}