const analyzerForm = document.querySelector("#analyzer-form");
const analyzerResult = document.querySelector("#analyzer-result");

const improvementForm =
    document.querySelector("#improvement-form");

const improvementResult =
    document.querySelector("#improvement-result");


    improvementForm.addEventListener(
        "submit",
        async (event) => {
            event.preventDefault();
    
            const prompt = document
                .querySelector("#improvement-prompt")
                .value
                .trim();
    
            setLoading(improvementForm, true);
    
            improvementResult.innerHTML =
                "<p>Improving prompt...</p>";
    
            try {
                const result = await postJson(
                    "/api/prompts/improve",
                    { prompt }
                );
    
                renderImprovementResult(result);
            } catch (error) {
                renderError(improvementResult, error);
            } finally {
                setLoading(improvementForm, false);
            }
        }
    );

analyzerForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const prompt =
        document.querySelector("#analyzer-prompt").value.trim();

    setLoading(analyzerForm, true);
    analyzerResult.innerHTML = "<p>Analyzing prompt...</p>";

    try {
        const result = await postJson(
            "/api/prompts/analyze",
            { prompt }
        );

        renderAnalyzerResult(result);
    } catch (error) {
        renderError(analyzerResult, error);
    } finally {
        setLoading(analyzerForm, false);
    }
});

const techniqueForm =
    document.querySelector("#technique-form");

const techniqueResult =
    document.querySelector("#technique-result");

    techniqueForm.addEventListener(
        "submit",
        async (event) => {
            event.preventDefault();
    
            const task = document
                .querySelector("#technique-task")
                .value
                .trim();
    
            setLoading(techniqueForm, true);
    
            techniqueResult.innerHTML =
                "<p>Choosing a technique...</p>";
    
            try {
                const result = await postJson(
                    "/api/techniques/recommend",
                    { task }
                );
    
                renderTechniqueResult(result);
            } catch (error) {
                renderError(techniqueResult, error);
            } finally {
                setLoading(techniqueForm, false);
            }
        }
    );
    
async function postJson(url, body) {
    const response = await fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    const data = await response.json();

    if (!response.ok) {
        const detailMessage = data.details
            ? Object.values(data.details)[0]
            : null;

        throw new Error(
            detailMessage || data.error || "The request failed."
        );
    }

    return data;
}

function renderTechniqueResult(result) {
    const alternatives = result.alternatives
        .map((alternative) => `
            <li>${escapeHtml(alternative)}</li>
        `)
        .join("");

    const signals = result.detectedSignals
        .map((signal) => `
            <li>${escapeHtml(signal)}</li>
        `)
        .join("");

    techniqueResult.innerHTML = `
        <h3>
            ${escapeHtml(result.recommendedTechnique)}
        </h3>

        <p>${escapeHtml(result.explanation)}</p>

        <h4>Why this technique?</h4>
        <ul>${signals}</ul>

        <h4>Alternatives</h4>
        <ul>${alternatives}</ul>
    `;
}

function renderAnalyzerResult(result) {
    const criteria = result.criteria
        .map((criterion) => `
            <li>
                <strong>
                    ${escapeHtml(criterion.criterion)}
                </strong>
                <span>
                    ${criterion.score}/${criterion.maximumScore}
                </span>
                <p>
                    ${escapeHtml(criterion.suggestion)}
                </p>
            </li>
        `)
        .join("");

    analyzerResult.innerHTML = `
        <h3>
            Score: ${result.overallScore}/${result.maximumScore}
        </h3>
        <p>
            Rating:
            <strong>${escapeHtml(result.rating)}</strong>
        </p>
        <ul>${criteria}</ul>
    `;
}

function renderImprovementResult(result) {
    const improvements = result.improvements
        .map((improvement) => `
            <li>${escapeHtml(improvement)}</li>
        `)
        .join("");

    improvementResult.innerHTML = `
        <h3>Improved prompt</h3>

        <pre>${escapeHtml(result.improvedPrompt)}</pre>

        <h4>Changes applied</h4>
        <ul>${improvements}</ul>
    `;
}

function renderError(container, error) {
    container.innerHTML = `
        <p role="alert">
            <strong>Error:</strong>
            ${escapeHtml(error.message)}
        </p>
    `;
}

function setLoading(form, loading) {
    const button = form.querySelector("button");

    if (!button.dataset.originalText) {
        button.dataset.originalText = button.textContent;
    }

    button.disabled = loading;
    button.textContent = loading
        ? "Working..."
        : button.dataset.originalText;
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}