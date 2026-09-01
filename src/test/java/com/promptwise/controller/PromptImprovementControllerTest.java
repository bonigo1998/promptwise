package com.promptwise.controller;

import com.promptwise.exception.GlobalExceptionHandler;
import com.promptwise.service.PromptImprovementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PromptImprovementController.class)
@Import({
        PromptImprovementService.class,
        GlobalExceptionHandler.class
})
class PromptImprovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldImproveValidPrompt() throws Exception {
        String requestBody = """
                {
                  "prompt": "Help me study"
                }
                """;

        mockMvc.perform(
                        post("/api/prompts/improve")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalPrompt")
                        .value("Help me study"))
                .andExpect(jsonPath("$.improvedPrompt")
                        .isString())
                .andExpect(jsonPath("$.improvedPrompt")
                        .value(
                                org.hamcrest.Matchers.containsString("Goal:")
                        ))
                .andExpect(jsonPath("$.improvements.length()")
                        .value(6));
    }

    @Test
    void shouldRejectBlankPrompt() throws Exception {
        String requestBody = """
                {
                  "prompt": ""
                }
                """;

        mockMvc.perform(
                        post("/api/prompts/improve")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.error")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.details.prompt")
                        .value("Prompt must not be blank"));
    }

    @Test
    void shouldRejectMissingPrompt() throws Exception {
        mockMvc.perform(
                        post("/api/prompts/improve")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.prompt")
                        .value("Prompt must not be blank"));
    }

    @Test
    void shouldRejectMalformedJson() throws Exception {
        mockMvc.perform(
                        post("/api/prompts/improve")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{invalid}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Malformed JSON"));
    }
}