package com.promptwise.service;

import com.promptwise.model.PromptImprovementResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptImprovementService {

    public PromptImprovementResponse improve(String prompt) {
        String cleanedPrompt = cleanPrompt(prompt);

        String improvedPrompt = """
                Role:
                You are an expert assistant for the requested task.

                Goal:
                %s

                Context:
                The intended audience is [describe the audience].
                Their experience level is [beginner, intermediate, or advanced].
                Relevant background information: [add useful context].

                Specific requirements:
                1. Focus on [main topic or objective].
                2. Include [required details or examples].
                3. Explain important concepts clearly.
                4. Make the response practical and actionable.

                Constraints:
                - Keep the response within [desired length].
                - Avoid [unwanted content or approaches].
                - Use a tone that is [professional, friendly, technical, or concise].

                Output format:
                Present the response as [numbered steps, bullet points, a table, JSON, or paragraphs].
                """.formatted(cleanedPrompt);

        List<String> improvements = List.of(
                "Added a defined assistant role",
                "Clarified the primary goal",
                "Added placeholders for audience and context",
                "Added specific requirements",
                "Added measurable constraints",
                "Added an explicit output format"
        );

        return new PromptImprovementResponse(
                prompt,
                improvedPrompt,
                improvements
        );
    }

    private String cleanPrompt(String prompt) {
        String cleaned = prompt.trim()
                .replaceAll("\\s+", " ");

        if (endsWithPunctuation(cleaned)) {
            return cleaned;
        }

        return cleaned + ".";
    }

    private boolean endsWithPunctuation(String value) {
        return value.endsWith(".")
                || value.endsWith("!")
                || value.endsWith("?");
    }
}