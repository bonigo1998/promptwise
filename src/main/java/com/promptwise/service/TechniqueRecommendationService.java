package com.promptwise.service;

import com.promptwise.model.TechniqueRecommendationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class TechniqueRecommendationService {

    public TechniqueRecommendationResponse recommend(String task) {
        String normalized = task.toLowerCase(Locale.ROOT);

        if (containsAny(
                normalized,
                "verify",
                "validate",
                "fact-check",
                "fact check",
                "audit",
                "check accuracy",
                "medical",
                "legal",
                "financial"
        )) {
            return createResponse(
                    task,
                    "Cognitive verifier",
                    "Generate an answer and then independently verify its accuracy.",
                    List.of("Chain-of-thought", "Tree-of-thought"),
                    "The task emphasizes verification or accuracy."
            );
        }

        if (containsAny(
                normalized,
                "multiple options",
                "several options",
                "alternative approaches",
                "trade-offs",
                "tradeoffs",
                "strategies",
                "best option",
                "pros and cons"
        )) {
            return createResponse(
                    task,
                    "Tree-of-thought",
                    "Explore multiple approaches, compare them, and select the strongest option.",
                    List.of(
                            "Chain-of-thought",
                            "Cognitive verifier"
                    ),
                    "The task requires comparing alternative approaches."
            );
        }

        if (containsAny(
                normalized,
                "examples",
                "example inputs",
                "example outputs",
                "follow this style",
                "match this format",
                "demonstrations",
                "sample responses"
        )) {
            return createResponse(
                    task,
                    "Few-shot",
                    "Provide representative examples that demonstrate the expected pattern.",
                    List.of("Zero-shot", "Chain-of-thought"),
                    "The task uses examples to establish a pattern."
            );
        }

        if (containsAny(
                normalized,
                "break down",
                "decompose",
                "prerequisite",
                "from basics",
                "learning path",
                "build progressively",
                "simple to complex"
        )) {
            return createResponse(
                    task,
                    "Least-to-most",
                    "Solve simpler prerequisite tasks before progressing to the complete task.",
                    List.of("Chain-of-thought", "Few-shot"),
                    "The task should be decomposed into progressive stages."
            );
        }

        if (containsAny(
                normalized,
                "solve",
                "calculate",
                "debug",
                "reason",
                "derive",
                "logic",
                "mathematical",
                "explain why",
                "root cause"
        )) {
            return createResponse(
                    task,
                    "Chain-of-thought",
                    "Use an ordered sequence of intermediate reasoning steps.",
                    List.of(
                            "Least-to-most",
                            "Cognitive verifier"
                    ),
                    "The task requires multi-step reasoning."
            );
        }

        return createResponse(
                task,
                "Zero-shot",
                "Give clear instructions and perform the task directly without examples.",
                List.of("Few-shot"),
                "The task appears straightforward."
        );
    }

    private TechniqueRecommendationResponse createResponse(
            String task,
            String technique,
            String explanation,
            List<String> alternatives,
            String detectedSignal
    ) {
        return new TechniqueRecommendationResponse(
                task,
                technique,
                explanation,
                alternatives,
                List.of(detectedSignal)
        );
    }

    private boolean containsAny(
            String text,
            String... signals
    ) {
        for (String signal : signals) {
            if (text.contains(signal)) {
                return true;
            }
        }

        return false;
    }
}