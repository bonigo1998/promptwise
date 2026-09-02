package com.promptwise.service;

import com.promptwise.model.BiasClassificationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BiasClassificationServiceTest {

    private BiasClassificationService service;

    @BeforeEach
    void setUp() {
        service = new BiasClassificationService();
    }

    @ParameterizedTest
    @MethodSource("biasScenarios")
    void shouldClassifyBiasScenario(
            String scenario,
            String expectedBias
    ) {
        BiasClassificationResponse response =
                service.classify(scenario);

        assertThat(response.biasType())
                .isEqualTo(expectedBias);

        assertThat(response.confidence())
                .isGreaterThan(0);

        assertThat(response.explanation())
                .isNotBlank();

        assertThat(response.detectedIndicators())
                .isNotEmpty();
    }

    static Stream<Arguments> biasScenarios() {
        return Stream.of(
                Arguments.of(
                        """
                        A national study used a small group from one
                        neighborhood, so the sample was not representative
                        of the population.
                        """,
                        "Sampling bias"
                ),
                Arguments.of(
                        """
                        A questionnaire used a leading question that
                        influenced how participants answered.
                        """,
                        "Measurement bias"
                ),
                Arguments.of(
                        """
                        The study only included volunteers who opted in
                        and excluded everyone else.
                        """,
                        "Selection bias"
                ),
                Arguments.of(
                        """
                        An automated hiring algorithm used biased training
                        data and ranked qualified women lower.
                        """,
                        "Algorithmic bias"
                ),
                Arguments.of(
                        """
                        The researcher searched for supporting evidence
                        to confirm an existing belief and ignored opposing
                        evidence.
                        """,
                        "Confirmation bias"
                )
        );
    }

    @Test
    void shouldReturnUnclearWhenNoIndicatorsArePresent() {
        BiasClassificationResponse response =
                service.classify(
                        "The team discussed the project during a meeting."
                );

        assertThat(response.biasType())
                .isEqualTo("Unclear");

        assertThat(response.confidence())
                .isZero();

        assertThat(response.detectedIndicators())
                .isEmpty();
    }

    @Test
    void shouldPreserveOriginalScenario() {
        String scenario =
                "An algorithm made an automated hiring decision.";

        BiasClassificationResponse response =
                service.classify(scenario);

        assertThat(response.scenario())
                .isEqualTo(scenario);
    }

    @Test
    void confidenceShouldNotExceedNinetyFive() {
        String scenario = """
                An algorithm used artificial intelligence and machine
                learning with biased training data in an automated
                ranking system and recommendation system.
                """;

        BiasClassificationResponse response =
                service.classify(scenario);

        assertThat(response.confidence())
                .isLessThanOrEqualTo(95);
    }
}