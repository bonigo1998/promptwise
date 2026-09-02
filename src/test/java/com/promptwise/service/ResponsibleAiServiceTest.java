package com.promptwise.service;

import com.promptwise.model.ResponsibleAiAssessment;
import com.promptwise.model.ResponsibleAiCheckResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ResponsibleAiServiceTest {

    private ResponsibleAiService service;

    @BeforeEach
    void setUp() {
        service = new ResponsibleAiService();
    }

    @ParameterizedTest
    @MethodSource("highRiskScenarios")
    void shouldDetectHighRiskForDimension(
            String scenario,
            String dimension
    ) {
        ResponsibleAiCheckResponse response =
                service.check(scenario);

        ResponsibleAiAssessment assessment =
                findAssessment(response, dimension);

        assertThat(assessment.riskLevel())
                .isEqualTo("High");

        assertThat(assessment.score())
                .isGreaterThanOrEqualTo(70);

        assertThat(assessment.findings())
                .isNotEmpty();

        assertThat(assessment.recommendations())
                .isNotEmpty();
    }

    static Stream<Arguments> highRiskScenarios() {
        return Stream.of(
                Arguments.of(
                        "The hiring system discriminates based on gender.",
                        "Fairness"
                ),
                Arguments.of(
                        "A black box system makes decisions without explanation.",
                        "Transparency"
                ),
                Arguments.of(
                        "The fully autonomous system has no human review.",
                        "Accountability"
                ),
                Arguments.of(
                        "Personal data is collected without consent and stored indefinitely.",
                        "Privacy"
                ),
                Arguments.of(
                        "A medical diagnosis system is deployed without testing.",
                        "Safety"
                ),
                Arguments.of(
                        "Mass surveillance is used to influence an election.",
                        "Societal impact"
                )
        );
    }

    @Test
    void shouldReturnSixAssessments() {
        ResponsibleAiCheckResponse response =
                service.check(
                        "A person uses a calculator to add grocery prices."
                );

        assertThat(response.assessments())
                .hasSize(6);

        assertThat(response.assessments())
                .extracting(ResponsibleAiAssessment::dimension)
                .containsExactly(
                        "Fairness",
                        "Transparency",
                        "Accountability",
                        "Privacy",
                        "Safety",
                        "Societal impact"
                );
    }

    @Test
    void shouldReturnLowRiskWhenNoIndicatorsExist() {
        ResponsibleAiCheckResponse response =
                service.check(
                        "A person uses a calculator to add grocery prices."
                );

        assertThat(response.overallRiskLevel())
                .isEqualTo("Low");

        assertThat(response.overallRiskScore())
                .isEqualTo(10);

        assertThat(response.assessments())
                .allSatisfy(assessment -> {
                    assertThat(assessment.riskLevel())
                            .isEqualTo("Low");
                    assertThat(assessment.score())
                            .isEqualTo(10);
                });
    }

    @Test
    void shouldUseHighestDimensionAsOverallRisk() {
        String scenario = """
                A fully autonomous hiring system uses historical hiring
                data and automatically rejects applicants without human
                review.
                """;

        ResponsibleAiCheckResponse response =
                service.check(scenario);

        int highestDimensionScore = response.assessments()
                .stream()
                .mapToInt(ResponsibleAiAssessment::score)
                .max()
                .orElseThrow();

        assertThat(response.overallRiskScore())
                .isEqualTo(highestDimensionScore);
    }

    @Test
    void shouldPreserveOriginalScenario() {
        String scenario =
                "An automated system collects personal data.";

        ResponsibleAiCheckResponse response =
                service.check(scenario);

        assertThat(response.scenario())
                .isEqualTo(scenario);
    }

    @Test
    void shouldIncludeDisclaimer() {
        ResponsibleAiCheckResponse response =
                service.check("A simple scenario.");

        assertThat(response.disclaimer())
                .isNotBlank()
                .containsIgnoringCase("educational")
                .containsIgnoringCase("does not replace");
    }

    private ResponsibleAiAssessment findAssessment(
            ResponsibleAiCheckResponse response,
            String dimension
    ) {
        return response.assessments()
                .stream()
                .filter(assessment ->
                        assessment.dimension().equals(dimension)
                )
                .findFirst()
                .orElseThrow();
    }
}