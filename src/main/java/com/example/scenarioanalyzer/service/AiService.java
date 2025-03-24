package com.example.scenarioanalyzer.service;

import com.example.scenarioanalyzer.model.ScenarioAnalysisRequest;
import com.example.scenarioanalyzer.model.ScenarioAnalysisResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AiService {
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiUrl;
    
    public AiService(
            OkHttpClient httpClient,
            @Value("${ollama.api.url}") String apiUrl) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
        this.apiUrl = apiUrl;
    }
    
    public ScenarioAnalysisResponse generateAnalysis(ScenarioAnalysisRequest request) {
        try {
            // First test if Ollama is responding
            String testResponse = callOllamaApi("Hello, are you working?");
            System.out.println("Test response: " + testResponse);
            
            // If test worked, send actual prompt
            String prompt = buildPrompt(request);
            String response = callOllamaApi(prompt);
            return parseResponse(response);
        } catch (Exception e) {
            System.out.println("Error generating analysis: " + e.getMessage());
            e.printStackTrace();
            return getFallbackResponse(request.getScenario());
        }
    }
    
    private String callOllamaApi(String prompt) throws IOException {
        String jsonBody = "{\"model\":\"mistral\",\"prompt\":\"" + prompt + "\"}";
        System.out.println("Sending to Ollama: " + jsonBody);
        
        RequestBody body = RequestBody.create(
            jsonBody, 
            MediaType.parse("application/json")
        );
        
        Request request = new Request.Builder()
            .url(apiUrl)
            .post(body)
            .build();
            
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            System.out.println("Ollama response: " + responseBody);
            
            if (!response.isSuccessful()) {
                throw new IOException("API call failed: " + response.code());
            }
            
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            return jsonResponse.get("response").asText();
        }
    }
    
    private String buildPrompt(ScenarioAnalysisRequest request) {
        return "Please analyze this scenario: " + request.getScenario() + 
               ". The constraints are: " + String.join(", ", request.getConstraints());
    }
    
    private ScenarioAnalysisResponse parseResponse(String response) {
        return ScenarioAnalysisResponse.builder()
            .scenarioSummary(response)
            .potentialPitfalls(List.of(
                "• Technical feasibility must be evaluated",
                "• Resource allocation needs careful planning",
                "• Timeline considerations are crucial"
            ))
            .proposedStrategies(List.of(
                "• Start with a minimum viable product (MVP)",
                "• Use agile development methodology",
                "• Implement regular progress reviews"
            ))
            .recommendedResources(List.of(
                "• Project management tools",
                "• Development frameworks"
            ))
            .disclaimer("This is an AI-generated analysis. Please validate with domain experts.")
            .build();
    }
    
    private ScenarioAnalysisResponse getFallbackResponse(String scenario) {
        return ScenarioAnalysisResponse.builder()
            .scenarioSummary("Analysis of: " + scenario)
            .potentialPitfalls(List.of(
                "• Technical feasibility must be evaluated",
                "• Resource allocation needs careful planning",
                "• Timeline considerations are crucial"
            ))
            .proposedStrategies(List.of(
                "• Start with a minimum viable product (MVP)",
                "• Use agile development methodology",
                "• Implement regular progress reviews"
            ))
            .recommendedResources(List.of(
                "• Project management tools",
                "• Development frameworks"
            ))
            .disclaimer("This is a fallback response due to API issues.")
            .build();
    }
} 