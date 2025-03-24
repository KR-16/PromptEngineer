package com.example.scenarioanalyzer.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScenarioAnalysisResponse {
    private String scenarioSummary;
    private List<String> potentialPitfalls;
    private List<String> proposedStrategies;
    private List<String> recommendedResources;
    private String disclaimer;
} 