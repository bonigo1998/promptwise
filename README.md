# PromptWise

PromptWise is a Java and Spring Boot toolkit for analyzing and improving AI prompts, recommending prompting techniques, identifying bias, and reviewing Responsible AI risks.

The application includes a REST API and a responsive browser interface. Its current evaluations use deterministic, explainable rules and do not require an external AI API or API key.

## Features

### Prompt Analyzer

Scores prompts across five dimensions:

- Clarity
- Specificity
- Context
- Constraints
- Output format

### Prompt Improvement Generator

Transforms a weak prompt into a structured template containing:

- Role
- Goal
- Context
- Specific requirements
- Constraints
- Output format

### Prompt Technique Recommender

Recommends one of the following techniques:

- Zero-shot
- Few-shot
- Chain-of-thought
- Tree-of-thought
- Cognitive verifier
- Least-to-most

### Bias Scenario Classifier

Classifies scenarios as:

- Sampling bias
- Measurement bias
- Selection bias
- Algorithmic bias
- Confirmation bias
- Unclear

### Responsible AI Checker

Reviews scenarios for:

- Fairness
- Transparency
- Accountability
- Privacy
- Safety
- Societal impact

## Technology

- Java 26
- Spring Boot 4.1.1
- Maven Wrapper
- Spring Web MVC
- Jakarta Validation
- Jackson
- HTML
- CSS
- JavaScript
- JUnit 5
- MockMvc
- AssertJ

## Requirements

Install:

- Java 26
- Git

Verify Java:

```bash
java --version
javac --version
```

Both commands should report Java 26.

Maven does not need to be installed separately because the repository includes the Maven Wrapper.

## Download PromptWise

Clone the repository:

```bash
git clone https://github.com/bonigo1998/promptwise.git
```

Enter the project:

```bash
cd promptwise
```

## Run the tests

On macOS or Linux:

```bash
./mvnw clean test
```

On Windows:

```powershell
mvnw.cmd clean test
```

A successful run ends with:

```text
BUILD SUCCESS
```

## Start PromptWise

On macOS or Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd spring-boot:run
```

Wait until the terminal displays:

```text
Tomcat started on port 8080
Started PromptwiseApplication
```

Keep that terminal open while using PromptWise.

## Access the browser interface

Open:

[http://localhost:8080/](http://localhost:8080/)

The browser interface provides forms for:

1. Prompt Analyzer
2. Prompt Improvement Generator
3. Prompt Technique Recommender
4. Bias Scenario Classifier
5. Responsible AI Checker

## Access the API

API overview:

[http://localhost:8080/api](http://localhost:8080/api)

Health check:

[http://localhost:8080/api/health](http://localhost:8080/api/health)

Expected health response:

```json
{
  "application": "PromptWise",
  "status": "UP",
  "version": "0.1.0"
}
```

## REST endpoints

| Feature | Method | Endpoint |
|---|---:|---|
| API overview | GET | `/api` |
| Health check | GET | `/api/health` |
| Prompt Analyzer | POST | `/api/prompts/analyze` |
| Prompt Improvement Generator | POST | `/api/prompts/improve` |
| Technique Recommender | POST | `/api/techniques/recommend` |
| Bias Classifier | POST | `/api/bias/classify` |
| Responsible AI Checker | POST | `/api/responsible-ai/check` |

## Prompt Analyzer API

Request:

```bash
curl -X POST http://localhost:8080/api/prompts/analyze \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Help me study"}'
```

Example response:

```json
{
  "prompt": "Help me study",
  "overallScore": 10,
  "maximumScore": 100,
  "rating": "Weak",
  "criteria": [
    {
      "criterion": "Clarity",
      "score": 10,
      "maximumScore": 20,
      "suggestion": "State a clear action using a verb such as create, explain, compare, or analyze."
    }
  ]
}
```

A more detailed prompt should receive a higher score:

```bash
curl -X POST http://localhost:8080/api/prompts/analyze \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Create a 7-day Java study plan for a beginner. Include daily exercises, limit each session to 60 minutes, and present the result as numbered steps."}'
```

## Prompt Improvement API

```bash
curl -X POST http://localhost:8080/api/prompts/improve \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Help me study"}'
```

The response contains:

- The original prompt
- A structured improved prompt
- A list of improvements applied

## Technique Recommender API

```bash
curl -X POST http://localhost:8080/api/techniques/recommend \
  -H "Content-Type: application/json" \
  -d '{"task":"Compare several architecture options and their pros and cons"}'
```

Expected recommendation:

```json
{
  "recommendedTechnique": "Tree-of-thought"
}
```

Other examples:

```text
Translate this paragraph into Spanish
→ Zero-shot

Classify reviews using these examples
→ Few-shot

Solve this mathematical problem
→ Chain-of-thought

Verify this financial calculation
→ Cognitive verifier

Create a learning path from basics
→ Least-to-most
```

## Bias Classifier API

```bash
curl -X POST http://localhost:8080/api/bias/classify \
  -H "Content-Type: application/json" \
  -d '{"scenario":"An automated hiring algorithm used biased training data and ranked qualified women lower."}'
```

Expected classification:

```json
{
  "biasType": "Algorithmic bias"
}
```

If the scenario contains insufficient evidence, the classifier returns:

```json
{
  "biasType": "Unclear",
  "confidence": 0
}
```

## Responsible AI Checker API

```bash
curl -X POST http://localhost:8080/api/responsible-ai/check \
  -H "Content-Type: application/json" \
  -d '{"scenario":"A fully autonomous hiring system rejects applicants without explanation or human review."}'
```

The response contains:

- Overall risk level
- Overall risk score
- Six dimension assessments
- Detected findings
- Recommended mitigations
- An educational-use disclaimer

## Validation errors

Blank, missing, or malformed inputs return HTTP `400`.

Example:

```json
{
  "timestamp": "2026-09-05T12:00:00Z",
  "status": 400,
  "error": "Validation failed",
  "details": {
    "prompt": "Prompt must not be blank"
  }
}
```

Malformed JSON returns:

```json
{
  "status": 400,
  "error": "Malformed JSON",
  "details": {
    "request": "Request body must contain valid JSON"
  }
}
```

## Build the executable JAR

Run:

```bash
./mvnw clean package
```

The packaged application is created at:

```text
target/promptwise-0.1.0.jar
```

Run it:

```bash
java -jar target/promptwise-0.1.0.jar
```

Then open:

[http://localhost:8080/](http://localhost:8080/)

## Use a different port

If port 8080 is already being used:

```bash
java -jar target/promptwise-0.1.0.jar --server.port=8081
```

Then open:

[http://localhost:8081/](http://localhost:8081/)

For Maven development mode:

```bash
./mvnw spring-boot:run \
  -Dspring-boot.run.arguments=--server.port=8081
```

## Stop PromptWise

Return to the terminal running the application and press:

```text
Control + C
```

## Project architecture

```text
Browser interface
       |
       | HTTP and JSON
       v
REST controllers
       |
       v
Rule-based services
       |
       v
Response models
       |
       v
JSON rendered in the browser
```

Package responsibilities:

- `controller`: Defines HTTP routes and validates incoming requests.
- `service`: Contains evaluation and classification rules.
- `model`: Defines request, response, and assessment records.
- `exception`: Converts errors into consistent JSON responses.
- `static`: Contains the HTML, CSS, and JavaScript interface.

## Project structure

```text
promptwise/
├── README.md
├── pom.xml
├── mvnw
├── mvnw.cmd
└── src/
    ├── main/
    │   ├── java/com/promptwise/
    │   │   ├── controller/
    │   │   ├── exception/
    │   │   ├── model/
    │   │   ├── service/
    │   │   └── PromptwiseApplication.java
    │   └── resources/
    │       ├── application.properties
    │       └── static/
    │           ├── index.html
    │           ├── styles.css
    │           └── app.js
    └── test/
        └── java/com/promptwise/
            ├── controller/
            ├── service/
            └── PromptwiseApplicationTests.java
```

## Run individual tests

Example:

```bash
./mvnw -Dtest=PromptAnalysisServiceTest test
```

Frontend resource tests:

```bash
./mvnw -Dtest=StaticResourcesTest test
```

Run the complete suite:

```bash
./mvnw clean test
```

## Troubleshooting

### Localhost refuses to connect

The server is not running. Start it:

```bash
./mvnw spring-boot:run
```

Keep the terminal open while using the application.

### The browser still shows an old page

Perform a hard refresh:

```text
Command + Shift + R
```

### A form reloads the page without displaying a result

Confirm that `index.html` contains exactly one script reference near the bottom of `<body>`:

```html
<script src="/app.js"></script>
```

### JavaScript cannot reach the server

Verify the health endpoint:

```bash
curl http://localhost:8080/api/health
```

If it fails, restart Spring Boot.

### Port 8080 is already occupied

Use port 8081:

```bash
./mvnw spring-boot:run \
  -Dspring-boot.run.arguments=--server.port=8081
```

### Permission denied for `mvnw`

On macOS or Linux:

```bash
chmod +x mvnw
```

Then retry:

```bash
./mvnw clean test
```

## Current limitations

- Evaluations use deterministic keyword and text-pattern rules.
- Confidence values are heuristic scores, not statistical probabilities.
- The classifier may return `Unclear` when it lacks sufficient indicators.
- Prompt improvement uses structured placeholders rather than generated domain knowledge.
- PromptWise does not replace legal, ethical, privacy, security, safety, or subject-matter review.
- Database persistence and optional LLM integration are possible future additions.

## Planned improvements

- GitHub Actions continuous integration
- OpenAPI and Swagger documentation
- SQLite or PostgreSQL evaluation history
- Export to JSON and Markdown
- Optional LLM-assisted evaluation
- Docker packaging
- Cloud deployment
- Authentication and rate limiting
- Dark mode and additional accessibility improvements

## Release

Current version:

```text
v0.1.0
```

Repository:

[https://github.com/bonigo1998/promptwise](https://github.com/bonigo1998/promptwise)

## License

No license has been selected yet.ies.
- The toolkit does not replace legal, ethical, privacy, safety, or subject-matter review.
- Database persistence and LLM integration are planned future additions.

## License

No license has been selected yet.
