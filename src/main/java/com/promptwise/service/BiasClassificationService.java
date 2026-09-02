package com.promptwise.service;

import com.promptwise.model.BiasClassificationResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class BiasClassificationService {

    private static final List<BiasRule> RULES = List.of(
            new BiasRule(
                    "Algorithmic bias",
                    List.of(
                            "algorithm",
                            "automated",
                            "artificial intelligence",
                            "ai system",
                            "machine learning",
                            "training data",
                            "ranking system",
                            "recommendation system",
                            "facial recognition"
                    )
            ),
            new BiasRule(
                    "Confirmation bias",
                    List.of(
                            "confirm",
                            "existing belief",
                            "already believed",
                            "supporting evidence",
                            "ignored contrary",
                            "ignored opposing",
                            "cherry-pick",
                            "cherry pick",
                            "only searched for"
                    )
            ),
            new BiasRule(
                    "Measurement bias",
                    List.of(
                            "measurement",
                            "measured",
                            "survey wording",
                            "leading question",
                            "questionnaire",
                            "sensor",
                            "instrument",
                            "self-reported",
                            "inaccurate scale",
                            "data collection method"
                    )
            ),
            new BiasRule(
                    "Selection bias",
                    List.of(
                            "selected",
                            "excluded",
                            "eligibility",
                            "volunteer",
                            "opted in",
                            "hiring process",
                            "admission process",
                            "survivors",
                            "only included",
                            "chosen participants"
                    )
            ),
            new BiasRule(
                    "Sampling bias",
                    List.of(
                            "sample",
                            "population",
                            "representative",
                            "surveyed only",
                            "single location",
                            "one neighborhood",
                            "one school",
                            "online poll",
                            "small group",
                            "convenience sample"
                    )
            )
    );

    public BiasClassificationResponse classify(String scenario) {
        String normalized =
                scenario.toLowerCase(Locale.ROOT).trim();

        BiasMatch bestMatch = RULES.stream()
                .map(rule -> evaluate(rule, normalized))
                .max((first, second) ->
                        Integer.compare(
                                first.indicators().size(),
                                second.indicators().size()
                        )
                )
                .orElseThrow();

        if (bestMatch.indicators().isEmpty()) {
            return new BiasClassificationResponse(
                    scenario,
                    "Unclear",
                    0,
                    "The scenario does not contain enough information to identify a bias category reliably.",
                    List.of()
            );
        }

        int confidence = Math.min(
                95,
                40 + bestMatch.indicators().size() * 15
        );

        return new BiasClassificationResponse(
                scenario,
                bestMatch.biasType(),
                confidence,
                explanationFor(bestMatch.biasType()),
                bestMatch.indicators()
        );
    }

    private BiasMatch evaluate(
            BiasRule rule,
            String scenario
    ) {
        List<String> indicators = new ArrayList<>();

        for (String keyword : rule.keywords()) {
            if (scenario.contains(keyword)) {
                indicators.add(keyword);
            }
        }

        return new BiasMatch(
                rule.biasType(),
                List.copyOf(indicators)
        );
    }

    private String explanationFor(String biasType) {
        return switch (biasType) {
            case "Sampling bias" ->
                    "The sample may not represent the broader population.";

            case "Measurement bias" ->
                    "The method used to measure or collect information may systematically distort the results.";

            case "Selection bias" ->
                    "The process used to include or exclude participants may influence the result.";

            case "Algorithmic bias" ->
                    "An automated system or its training data may produce systematically unfair outcomes.";

            case "Confirmation bias" ->
                    "Evidence may have been selected or interpreted to support an existing belief.";

            default ->
                    "The available information does not clearly identify a bias category.";
        };
    }

    private record BiasRule(
            String biasType,
            List<String> keywords
    ) {
    }

    private record BiasMatch(
            String biasType,
            List<String> indicators
    ) {
    }
}