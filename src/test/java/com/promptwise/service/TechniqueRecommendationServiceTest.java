package com.promptwise.service;

import com.promptwise.model.TechniqueRecommendationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class TechniqueRecommendationServiceTest {

    private TechniqueRecommendationService service;

    @BeforeEach
    void setUp() {
        service = new TechniqueRecommendationService();
    }

    @ParameterizedTest
    @MethodSource("techniqueCases")
    void shouldRecommendExpectedTechnique(
            String task,
            String expectedTechnique
    ) {
        TechniqueRecommendationResponse response =
                service.recommend(task);

        assertThat(response.recommendedTechnique())
                .isEqualTo(expectedTechnique);

        assertThat(response.explanation())
                .isNotBlank();

        assertThat(response.detectedSignals())
                .isNotEmpty();
    }

    static Stream<Arguments> techniqueCases() {
        return Stream.of(
                Arguments.of(
                        "Translate this paragraph into Spanish",
                        "Zero-shot"
                ),
                Arguments.of(
                        "Classify reviews using these example inputs and outputs",
                        "Few-shot"
                ),
                Arguments.of(
                        "Solve this mathematical problem",
                        "Chain-of-thought"
                ),
                Arguments.of(
                        "Compare several options and their pros and cons",
                        "Tree-of-thought"
                ),
                Arguments.of(
                        "Verify the accuracy of this financial calculation",
                        "Cognitive verifier"
                ),
                Arguments.of(
                        "Create a learning path from basics that builds progressively",
                        "Least-to-most"
                )
        );
    }

    @Test
    void shouldPrioritizeVerificationOverGeneralReasoning() {
        TechniqueRecommendationResponse response =
                service.recommend(
                        "Solve this calculation and verify its accuracy"
                );

        assertThat(response.recommendedTechnique())
                .isEqualTo("Cognitive verifier");
    }

    @Test
    void shouldPreserveOriginalTask() {
        String task = "Translate this paragraph into Spanish";

        TechniqueRecommendationResponse response =
                service.recommend(task);

        assertThat(response.task()).isEqualTo(task);
    }

    @Test
    void shouldProvideAlternativeTechnique() {
        TechniqueRecommendationResponse response =
                service.recommend("Solve this logic problem");

        assertThat(response.alternatives())
                .isNotEmpty()
                .doesNotContain(response.recommendedTechnique());
    }
}