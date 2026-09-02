package com.promptwise.model;

import java.util.Map;

public record ApiOverviewResponse(
        String application,
        String version,
        String status,
        Map<String, String> endpoints
) {
}