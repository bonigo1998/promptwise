package com.promptwise.controller;

import com.promptwise.model.TechniqueRecommendationRequest;
import com.promptwise.model.TechniqueRecommendationResponse;
import com.promptwise.service.TechniqueRecommendationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/techniques")
public class TechniqueRecommendationController {

    private final TechniqueRecommendationService service;

    public TechniqueRecommendationController(
            TechniqueRecommendationService service
    ) {
        this.service = service;
    }

    @PostMapping("/recommend")
    public TechniqueRecommendationResponse recommend(
            @Valid @RequestBody
            TechniqueRecommendationRequest request
    ) {
        return service.recommend(request.task());
    }
}