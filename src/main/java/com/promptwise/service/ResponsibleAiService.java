package com.promptwise.service;

import com.promptwise.model.ResponsibleAiAssessment;
import com.promptwise.model.ResponsibleAiCheckResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ResponsibleAiService {

    public ResponsibleAiCheckResponse check(String scenario) {
        String normalized =
                scenario.toLowerCase(Locale.ROOT).trim();

        List<ResponsibleAiAssessment> assessments = List.of(
                assess(
                        "Fairness",
                        normalized,
                        List.of(
                                "discriminat",
                                "biased",
                                "race",
                                "gender",
                                "protected characteristic",
                                "historical hiring data",
                                "unequal",
                                "hiring",
                                "loan approval",
                                "admission"
                        ),
                        List.of(
                                "Test outcomes across demographic groups.",
                                "Review training data for underrepresentation.",
                                "Provide a process for appealing unfair outcomes."
                        )
                ),
                assess(
                        "Transparency",
                        normalized,
                        List.of(
                                "without explanation",
                                "cannot explain",
                                "black box",
                                "secret",
                                "undisclosed",
                                "users are not told",
                                "automated decision"
                        ),
                        List.of(
                                "Tell affected users when AI is being used.",
                                "Explain the main factors behind decisions.",
                                "Document system capabilities and limitations."
                        )
                ),
                assess(
                        "Accountability",
                        normalized,
                        List.of(
                                "no human review",
                                "without human review",
                                "no appeal",
                                "without oversight",
                                "nobody responsible",
                                "fully autonomous",
                                "automatic rejection"
                        ),
                        List.of(
                                "Assign a named owner responsible for outcomes.",
                                "Require human review for consequential decisions.",
                                "Create an accessible appeal and correction process."
                        )
                ),
                assess(
                        "Privacy",
                        normalized,
                        List.of(
                                "without consent",
                                "personal data",
                                "biometric",
                                "facial recognition",
                                "location data",
                                "indefinitely",
                                "surveillance",
                                "tracks users",
                                "shares data",
                                "collects data"
                        ),
                        List.of(
                                "Collect only data necessary for the stated purpose.",
                                "Obtain informed consent where appropriate.",
                                "Set retention limits and protect stored information."
                        )
                ),
                assess(
                        "Safety",
                        normalized,
                        List.of(
                                "physical harm",
                                "unsafe",
                                "medical diagnosis",
                                "weapon",
                                "critical infrastructure",
                                "unverified",
                                "no testing",
                                "without testing",
                                "real-time decision"
                        ),
                        List.of(
                                "Test the system before deployment.",
                                "Add human intervention and shutdown mechanisms.",
                                "Monitor failures and document safety incidents."
                        )
                ),
                assess(
                        "Societal impact",
                        normalized,
                        List.of(
                                "mass surveillance",
                                "job displacement",
                                "misinformation",
                                "manipulation",
                                "election",
                                "vulnerable communities",
                                "public services",
                                "large-scale",
                                "large scale"
                        ),
                        List.of(
                                "Evaluate effects on affected communities.",
                                "Consult stakeholders before large-scale deployment.",
                                "Monitor long-term social and economic consequences."
                        )
                )
        );

        int overallScore = assessments.stream()
                .mapToInt(ResponsibleAiAssessment::score)
                .max()
                .orElse(0);

        return new ResponsibleAiCheckResponse(
                scenario,
                riskLevel(overallScore),
                overallScore,
                assessments,
                """
                This rule-based assessment is an educational screening tool. \
                It does not replace legal, ethical, security, privacy, or \
                domain-expert review.
                """.strip()
        );
    }

    private ResponsibleAiAssessment assess(
            String dimension,
            String scenario,
            List<String> riskIndicators,
            List<String> recommendations
    ) {
        List<String> matches = new ArrayList<>();

        for (String indicator : riskIndicators) {
            if (scenario.contains(indicator)) {
                matches.add(indicator);
            }
        }

        if (matches.isEmpty()) {
            return new ResponsibleAiAssessment(
                    dimension,
                    "Low",
                    10,
                    List.of(
                            "No explicit risk indicators were detected."
                    ),
                    List.of(
                            "Continue monitoring this dimension as the system evolves."
                    )
            );
        }

        int score = Math.min(
                95,
                40 + matches.size() * 15
        );

        List<String> findings = matches.stream()
                .map(match ->
                        "Detected risk indicator: " + match
                )
                .toList();

        return new ResponsibleAiAssessment(
                dimension,
                riskLevel(score),
                score,
                findings,
                recommendations
        );
    }

    private String riskLevel(int score) {
        if (score >= 70) {
            return "High";
        }

        if (score >= 40) {
            return "Medium";
        }

        return "Low";
    }
}