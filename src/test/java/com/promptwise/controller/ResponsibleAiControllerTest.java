package com.promptwise.controller;

import com.promptwise.exception.GlobalExceptionHandler;
import com.promptwise.service.ResponsibleAiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResponsibleAiController.class)
@Import({
        ResponsibleAiService.class,
        GlobalExceptionHandler.class
})
class ResponsibleAiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCheckHighRiskScenario() throws Exception {
        String requestBody = """
                {
                  "scenario": "A fully autonomous hiring system uses historical hiring data and automatically rejects applicants without human review."
                }
                """;

        mockMvc.perform(
                        post("/api/responsible-ai/check")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallRiskLevel")
                        .value("High"))
                .andExpect(jsonPath("$.overallRiskScore")
                        .isNumber())
                .andExpect(jsonPath("$.assessments.length()")
                        .value(6))
                .andExpect(jsonPath("$.disclaimer")
                        .isNotEmpty());
    }

    @Test
    void shouldCheckLowRiskScenario() throws Exception {
        String requestBody = """
                {
                  "scenario": "A person uses a calculator to add grocery prices."
                }
                """;

        mockMvc.perform(
                        post("/api/responsible-ai/check")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallRiskLevel")
                        .value("Low"))
                .andExpect(jsonPath("$.overallRiskScore")
                        .value(10))
                .andExpect(jsonPath("$.assessments.length()")
                        .value(6));
    }

    @Test
    void shouldRejectBlankScenario() throws Exception {
        String requestBody = """
                {
                  "scenario": ""
                }
                """;

        mockMvc.perform(
                        post("/api/responsible-ai/check")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.error")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.details.scenario")
                        .value("Scenario must not be blank"));
    }

    @Test
    void shouldRejectMissingScenario() throws Exception {
        mockMvc.perform(
                        post("/api/responsible-ai/check")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.scenario")
                        .value("Scenario must not be blank"));
    }

    @Test
    void shouldRejectMalformedJson() throws Exception {
        mockMvc.perform(
                        post("/api/responsible-ai/check")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{invalid}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status")
                        .value(400))
                .andExpect(jsonPath("$.error")
                        .value("Malformed JSON"))
                .andExpect(jsonPath("$.details.request")
                        .value(
                                "Request body must contain valid JSON"
                        ));
    }
}