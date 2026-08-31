package com.promptwise.service;

import com.promptwise.model.PromptAnalysisResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PromptAnalysisServiceTest {

    private PromptAnalysisService service;

    @BeforeEach
    void setUp() {
        service = new PromptAnalysisService();
    }

    @Test
    void shouldGiveWeakPromptALowScore() {
        PromptAnalysisResponse response =
                service.analyze("Help me study");

        assertThat(response.overallScore()).isLessThan(40);
        assertThat(response.rating()).isEqualTo("Weak");
        assertThat(response.criteria()).hasSize(5);
    }

    @Test
    void shouldGiveDetailedPromptAHighScore() {
        String prompt = """
                Create a 7-day study plan for a beginner learning Java.
                Include daily exercises, limit each session to 60 minutes,
                avoid advanced frameworks, and present the result as a
                numbered list.
                """;

        PromptAnalysisResponse response = service.analyze(prompt);

        assertThat(response.overallScore()).isGreaterThanOrEqualTo(80);
        assertThat(response.rating()).isEqualTo("Excellent");
        assertThat(response.criteria()).hasSize(5);
    }

    @Test
    void shouldAlwaysUseOneHundredAsMaximumScore() {
        PromptAnalysisResponse response =
                service.analyze("Explain Java.");

        assertThat(response.maximumScore()).isEqualTo(100);
    }

    @Test
    void shouldReturnAllExpectedCriteria() {
        PromptAnalysisResponse response =
                service.analyze("Create a Java study plan.");

        assertThat(response.criteria())
                .extracting(criterion -> criterion.criterion())
                .containsExactly(
                        "Clarity",
                        "Specificity",
                        "Context",
                        "Constraints",
                        "Output Format"
                );
    }

    @Test
    void shouldRecommendAnOutputFormatWhenMissing() {
        PromptAnalysisResponse response =
                service.analyze("Explain object-oriented programming.");

        assertThat(response.criteria())
                .filteredOn(
                        criterion ->
                                criterion.criterion()
                                        .equals("Output Format")
                )
                .singleElement()
                .satisfies(criterion -> {
                    assertThat(criterion.score()).isZero();
                    assertThat(criterion.suggestion())
                            .containsIgnoringCase("format");
                });
    }
}