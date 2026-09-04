package com.promptwise.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StaticResourcesTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldServeHomepage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("<title>PromptWise</title>")
                ))
                .andExpect(content().string(
                        containsString("id=\"analyzer-form\"")
                ))
                .andExpect(content().string(
                        containsString("id=\"responsible-ai-form\"")
                ));
    }

    @Test
    void shouldServeStylesheet() throws Exception {
        mockMvc.perform(get("/styles.css"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("--primary")
                ))
                .andExpect(content().string(
                        containsString(".assessment")
                ));
    }

    @Test
    void shouldServeJavaScript() throws Exception {
        mockMvc.perform(get("/app.js"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("/api/prompts/analyze")
                ))
                .andExpect(content().string(
                        containsString("/api/bias/classify")
                ))
                .andExpect(content().string(
                        containsString("/api/responsible-ai/check")
                ));
    }
}