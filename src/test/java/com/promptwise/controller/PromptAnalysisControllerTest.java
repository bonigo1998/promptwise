package com.promptwise.controller;

import com.promptwise.service.PromptAnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PromptAnalysisController.class)
@Import(PromptAnalysisService.class)
class PromptAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAnalyzeValidPrompt() throws Exception {
        String requestBody = """
                {
                  "prompt": "Help me study"
                }
                """;

        mockMvc.perform(
                        post("/api/prompts/analyze")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prompt")
                        .value("Help me study"))
                .andExpect(jsonPath("$.overallScore")
                        .isNumber())
                .andExpect(jsonPath("$.maximumScore")
                        .value(100))
                .andExpect(jsonPath("$.rating")
                        .value("Weak"))
                .andExpect(jsonPath("$.criteria.length()")
                        .value(5));
    }

    @Test
    void shouldRejectBlankPrompt() throws Exception {
        String requestBody = """
                {
                  "prompt": ""
                }
                """;

        mockMvc.perform(
                        post("/api/prompts/analyze")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectMissingPrompt() throws Exception {
        mockMvc.perform(
                        post("/api/prompts/analyze")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectInvalidJson() throws Exception {
        mockMvc.perform(
                        post("/api/prompts/analyze")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{invalid}")
                )
                .andExpect(status().isBadRequest());
    }
}