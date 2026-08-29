package com.promptwise.controller;

import com.promptwise.model.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController
{
    @GetMapping("/health")
    public HealthResponse health() 
    {
        return new HealthResponse
        (
            "PromptWise", 
            "UP",
            "0.0.1"
            );
        }
}