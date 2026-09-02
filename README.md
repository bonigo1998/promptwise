# PromptWise

PromptWise is a Java and Spring Boot toolkit for analyzing, improving, and evaluating AI prompts and Responsible AI scenarios.

The current implementation uses deterministic, explainable rules and does not require an external AI API.

## Features

- Prompt Analyzer
  - Scores clarity, specificity, context, constraints, and output format
- Prompt Improvement Generator
  - Converts weak prompts into structured prompt templates
- Prompt Technique Recommender
  - Recommends zero-shot, few-shot, chain-of-thought, tree-of-thought, cognitive verifier, or least-to-most
- Bias Scenario Classifier
  - Detects sampling, measurement, selection, algorithmic, and confirmation bias
- Responsible AI Checker
  - Reviews fairness, transparency, accountability, privacy, safety, and societal impact
- Consistent JSON validation errors
- Unit and REST controller tests

## Technology

- Java 26
- Spring Boot 4.1.1
- Maven
- Spring Web MVC
- Jakarta Validation
- Jackson
- JUnit 5
- MockMvc
- AssertJ

## Requirements

Verify that Java and Maven are available:

```bash
java --version
./mvnw --version
```

PromptWise targets Java 26.

## Run locally

Clone the repository:

```bash
git clone https://github.com/bonigo1998/promptwise.git
cd promptwise
```

Run the tests:

```bash
./mvnw clean test
```

Start the application:

```bash
./mvnw spring-boot:run
```

The server runs at:

```text
http://localhost:8080
```

## API overview

Open:

```text
GET http://localhost:8080/api
```

Health check:

```text
GET http://localhost:8080/api/health
```

## Endpoints

| Feature | Method | Endpoint |
|---|---:|---|
| API overview | GET | `/api` |
| Health check | GET | `/api/health` |
| Prompt analyzer | POST | `/api/prompts/analyze` |
| Prompt improvement | POST | `/api/prompts/improve` |
| Technique recommender | POST | `/api/techniques/recommend` |
| Bias classifier | POST | `/api/bias/classify` |
| Responsible AI checker | POST | `/api/responsible-ai/check` |

## Prompt Analyzer

Request:

```bash
curl -X POST http://localhost:8080/api/prompts/analyze \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Help me study"}'
```

The response includes an overall score and individual assessments for:

- Clarity
- Specificity
- Context
- Constraints
- Output format

## Prompt Improvement Generator

```bash
curl -X POST http://localhost:8080/api/prompts/improve \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Help me study"}'
```

The response contains a structured prompt with a role, goal, context, requirements, constraints, and output format.

## Prompt Technique Recommender

```bash
curl -X POST http://localhost:8080/api/techniques/recommend \
  -H "Content-Type: application/json" \
  -d '{"task":"Compare several options and their pros and cons"}'
```

The response recommends a prompting technique and explains the detected signals.

## Bias Scenario Classifier

```bash
curl -X POST http://localhost:8080/api/bias/classify \
  -H "Content-Type: application/json" \
  -d '{"scenario":"An automated hiring algorithm used biased training data."}'
```

Possible classifications include:

- Sampling bias
- Measurement bias
- Selection bias
- Algorithmic bias
- Confirmation bias
- Unclear

## Responsible AI Checker

```bash
curl -X POST http://localhost:8080/api/responsible-ai/check \
  -H "Content-Type: application/json" \
  -d '{"scenario":"A fully autonomous hiring system rejects applicants without explanation or human review."}'
```

The response evaluates:

- Fairness
- Transparency
- Accountability
- Privacy
- Safety
- Societal impact

## Validation errors

Invalid requests return consistent JSON:

```json
{
  "timestamp": "2026-09-02T12:00:00Z",
  "status": 400,
  "error": "Validation failed",
  "details": {
    "prompt": "Prompt must not be blank"
  }
}
```

## Project structure

```text
src/
├── main/
│   ├── java/com/promptwise/
│   │   ├── controller/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── service/
│   │   └── PromptwiseApplication.java
│   └── resources/
└── test/
    └── java/com/promptwise/
        ├── controller/
        └── service/
```

## Testing

Run all tests:

```bash
./mvnw clean test
```

Run one test class:

```bash
./mvnw -Dtest=BiasClassificationServiceTest test
```

## Current limitations

- Classification is based on keywords and explainable rules.
- Confidence scores are heuristic rather than statistical probabilities.
- The toolkit does not replace legal, ethical, privacy, safety, or subject-matter review.
- Database persistence and LLM integration are planned future additions.

## License

No license has been selected yet.
