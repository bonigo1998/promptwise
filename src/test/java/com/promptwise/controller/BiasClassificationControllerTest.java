package com.promptwise.controller;

import com.promptwise.exception.GlobalExceptionHandler;
import com.promptwise.service.BiasClassificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BiasClassificationController.class)
@Import({
        BiasClassificationService.class,
        GlobalExceptionHandler.class
})
class BiasClassificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldClassifyAlgorithmicBias() throws Exception {
        String requestBody = """
                {
                  "scenario": "An automated hiring algorithm used biased training data."
                }
                """;

        mockMvc.perform(
                        post("/api/bias/classify")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scenario")
                        .value(
                                "An automated hiring algorithm used biased training data."
                        ))
                .andExpect(jsonPath("$.biasType")
                        .value("Algorithmic bias"))
                .andExpect(jsonPath("$.confidence")
                        .isNumber())
                .andExpect(jsonPath("$.explanation")
                        .isNotEmpty())
                .andExpect(jsonPath("$.detectedIndicators")
                        .isArray());
    }

    @Test
    void shouldReturnUnclearForScenarioWithoutIndicators()
            throws Exception {
        String requestBody = """
                {
                  "scenario": "The team discussed the project during a meeting."
                }
                """;

        mockMvc.perform(
                        post("/api/bias/classify")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.biasType")
                        .value("Unclear"))
                .andExpect(jsonPath("$.confidence")
                        .value(0))
                .andExpect(jsonPath("$.detectedIndicators")
                        .isEmpty());
    }

    @Test
    void shouldRejectBlankScenario() throws Exception {
        String requestBody = """
                {
                  "scenario": ""
                }
                """;

        mockMvc.perform(
                        post("/api/bias/classify")
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
                        post("/api/bias/classify")
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
                        post("/api/bias/classify")
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