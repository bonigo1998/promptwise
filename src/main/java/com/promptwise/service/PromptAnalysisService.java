package com.promptwise.service;

import com.promptwise.model.CriterionResult;
import com.promptwise.model.PromptAnalysisResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class PromptAnalysisService {

    public PromptAnalysisResponse analyze(String prompt) {
        String normalizedPrompt = prompt.toLowerCase(Locale.ROOT).trim();
        int wordCount = countWords(normalizedPrompt);

        CriterionResult clarity =
                evaluateClarity(normalizedPrompt, wordCount);

        CriterionResult specificity =
                evaluateSpecificity(normalizedPrompt, wordCount);

        CriterionResult context =
                evaluateContext(normalizedPrompt, wordCount);

        CriterionResult constraints =
                evaluateConstraints(normalizedPrompt);

        CriterionResult outputFormat =
                evaluateOutputFormat(normalizedPrompt);

        List<CriterionResult> criteria = List.of(
                clarity,
                specificity,
                context,
                constraints,
                outputFormat
        );

        int overallScore = criteria.stream()
                .mapToInt(CriterionResult::score)
                .sum();

        return new PromptAnalysisResponse(
                prompt,
                overallScore,
                100,
                determineRating(overallScore),
                criteria
        );
    }

    private CriterionResult evaluateClarity(
            String prompt,
            int wordCount
    ) {
        int score = 0;

        if (wordCount >= 5) {
            score += 10;
        }

        if (containsAny(
                prompt,
                "create",
                "explain",
                "analyze",
                "compare",
                "summarize",
                "write",
                "generate",
                "identify",
                "recommend",
                "help"
        )) {
            score += 10;
        }

        String suggestion = score == 20
                ? "The requested action is clear."
                : "State a clear action using a verb such as create, explain, compare, or analyze.";

        return new CriterionResult(
                "Clarity",
                score,
                20,
                suggestion
        );
    }

    private CriterionResult evaluateSpecificity(
            String prompt,
            int wordCount
    ) {
        int score = 0;

        if (wordCount >= 12) {
            score += 10;
        }

        if (prompt.matches(".*\\d+.*")
                || containsAny(
                        prompt,
                        "specifically",
                        "include",
                        "focus on",
                        "example",
                        "topic",
                        "audience"
                )) {
            score += 10;
        }

        String suggestion = score == 20
                ? "The prompt contains useful specific details."
                : "Add concrete details, quantities, examples, or topics.";

        return new CriterionResult(
                "Specificity",
                score,
                20,
                suggestion
        );
    }

    private CriterionResult evaluateContext(
            String prompt,
            int wordCount
    ) {
        int score = 0;

        if (wordCount >= 20) {
            score += 10;
        }

        if (containsAny(
                prompt,
                "for a",
                "for an",
                "because",
                "background",
                "beginner",
                "intermediate",
                "advanced",
                "audience",
                "purpose"
        )) {
            score += 10;
        }

        String suggestion = score == 20
                ? "The prompt provides sufficient context."
                : "Explain the audience, background, skill level, or purpose.";

        return new CriterionResult(
                "Context",
                score,
                20,
                suggestion
        );
    }

    private CriterionResult evaluateConstraints(String prompt) {
        int score = 0;

        if (containsAny(
                prompt,
                "must",
                "should",
                "avoid",
                "limit",
                "within",
                "at most",
                "no more than",
                "do not"
        )) {
            score += 10;
        }

        if (prompt.matches(".*\\d+.*")) {
            score += 10;
        }

        String suggestion = score == 20
                ? "The prompt includes measurable constraints."
                : "Add boundaries such as length, deadline, required content, or exclusions.";

        return new CriterionResult(
                "Constraints",
                score,
                20,
                suggestion
        );
    }

    private CriterionResult evaluateOutputFormat(String prompt) {
        int score = containsAny(
                prompt,
                "format",
                "table",
                "list",
                "bullet",
                "json",
                "markdown",
                "paragraph",
                "steps",
                "outline"
        ) ? 20 : 0;

        String suggestion = score == 20
                ? "The desired output format is specified."
                : "Specify an output format such as a table, list, JSON, or numbered steps.";

        return new CriterionResult(
                "Output Format",
                score,
                20,
                suggestion
        );
    }

    private boolean containsAny(
            String prompt,
            String... terms
    ) {
        for (String term : terms) {
            if (prompt.contains(term)) {
                return true;
            }
        }

        return false;
    }

    private int countWords(String prompt) {
        if (prompt.isBlank()) {
            return 0;
        }

        return prompt.split("\\s+").length;
    }

    private String determineRating(int score) {
        if (score >= 80) {
            return "Excellent";
        }

        if (score >= 60) {
            return "Good";
        }

        if (score >= 40) {
            return "Needs Improvement";
        }

        return "Weak";
    }
}