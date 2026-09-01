package com.promptwise.controller;

import com.promptwise.exception.GlobalExceptionHandler;
import com.promptwise.service.TechniqueRecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TechniqueRecommendationController.class)
@Import({
        TechniqueRecommendationService.class,
        GlobalExceptionHandler.class
})
class TechniqueRecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRecommendTreeOfThought() throws Exception {
        String requestBody = """
                {
                  "task": "Compare several options and their pros and cons"
                }
                """;

        mockMvc.perform(
                        post("/api/techniques/recommend")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task")
                        .value(
                                "Compare several options and their pros and cons"
                        ))
                .andExpect(jsonPath("$.recommendedTechnique")
                        .value("Tree-of-thought"))
                .andExpect(jsonPath("$.explanation")
                        .isNotEmpty())
                .andExpect(jsonPath("$.alternatives")
                        .isArray())
                .andExpect(jsonPath("$.detectedSignals")
                        .isArray());
    }

    @Test
    void shouldRejectBlankTask() throws Exception {
        String requestBody = """
                {
                  "task": ""
                }
                """;

        mockMvc.perform(
                        post("/api/techniques/recommend")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.error")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.details.task")
                        .value("Task description must not be blank"));
    }

    @Test
    void shouldRejectMissingTask() throws Exception {
        mockMvc.perform(
                        post("/api/techniques/recommend")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.task")
                        .value("Task description must not be blank"));
    }

    @Test
    void shouldRejectMalformedJson() throws Exception {
        mockMvc.perform(
                        post("/api/techniques/recommend")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{invalid}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Malformed JSON"));
    }
}