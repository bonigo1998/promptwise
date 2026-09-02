package com.promptwise.controller;

import com.promptwise.model.BiasClassificationRequest;
import com.promptwise.model.BiasClassificationResponse;
import com.promptwise.service.BiasClassificationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bias")
public class BiasClassificationController {

    private final BiasClassificationService service;

    public BiasClassificationController(
            BiasClassificationService service
    ) {
        this.service = service;
    }

    @PostMapping("/classify")
    public BiasClassificationResponse classify(
            @Valid @RequestBody
            BiasClassificationRequest request
    ) {
        return service.classify(request.scenario());
    }
}