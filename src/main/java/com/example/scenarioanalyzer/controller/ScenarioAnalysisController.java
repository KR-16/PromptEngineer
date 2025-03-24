package com.example.scenarioanalyzer.controller;

import com.example.scenarioanalyzer.model.ScenarioAnalysisRequest;
import com.example.scenarioanalyzer.model.ScenarioAnalysisResponse;
import com.example.scenarioanalyzer.service.AiService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ScenarioAnalysisController {
    
    private final AiService aiService;
    
    public ScenarioAnalysisController(AiService aiService) {
        this.aiService = aiService;
    }
    
    @PostMapping("/analyze-scenario")
    public ResponseEntity<ScenarioAnalysisResponse> analyzeScenario(
            @Valid @RequestBody ScenarioAnalysisRequest request) {
        ScenarioAnalysisResponse response = aiService.generateAnalysis(request);
        return ResponseEntity.ok(response);
    }
} 