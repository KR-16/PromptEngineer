package com.example.scenarioanalyzer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ScenarioAnalysisRequest {
    @NotBlank(message = "Scenario cannot be empty")
    private String scenario;
    
    @NotEmpty(message = "At least one constraint must be provided")
    private List<String> constraints;
} 