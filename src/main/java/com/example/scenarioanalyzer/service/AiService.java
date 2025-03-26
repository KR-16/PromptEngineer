// package com.example.scenarioanalyzer.service;

// import com.example.scenarioanalyzer.model.ScenarioAnalysisRequest;
// import com.example.scenarioanalyzer.model.ScenarioAnalysisResponse;
// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import okhttp3.*;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// import java.io.IOException;
// import java.util.Arrays;
// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// public class AiService {
    
//     private final OkHttpClient httpClient;
//     private final ObjectMapper objectMapper;
//     private final String apiUrl;
    
//     public AiService(
//             OkHttpClient httpClient,
//             @Value("${ollama.api.url}") String apiUrl) {
//         this.httpClient = httpClient;
//         this.objectMapper = new ObjectMapper();
//         this.apiUrl = apiUrl;
//     }
    
//     public ScenarioAnalysisResponse generateAnalysis(ScenarioAnalysisRequest request) {
//         try {
//             // First test if Ollama is responding
//             String testResponse = callOllamaApi("Hello, are you working?");
//             System.out.println("Test response: " + testResponse);
            
//             // If test worked, send actual prompt
//             String prompt = buildPrompt(request);
//             String response = callOllamaApi(prompt);
//             return parseResponse(response);
//         } catch (Exception e) {
//             System.out.println("Error generating analysis: " + e.getMessage());
//             e.printStackTrace();
//             return getFallbackResponse(request.getScenario());
//         }
//     }

//     private String buildPrompt(ScenarioAnalysisRequest request) {
//         String sanitizedScenario = validateAndSanitizeInput(request.getScenario());
//         String sanitizedConstraints = request.getConstraints().stream()
//                 .map(this::validateAndSanitizeInput)
//                 .filter(s -> !s.isEmpty())
//                 .collect(Collectors.joining(", "));

//         return """
//             You are a scenario analysis assistant. Please analyze the following scenario and constraints carefully.
//             If you detect any legal implications, highlight them specifically.
            
//             SCENARIO:
//             %s
            
//             CONSTRAINTS:
//             %s
            
//             Please provide your analysis in the following structure:
//             1. Scenario Overview
//             2. Legal Considerations and Warnings
//             3. Technical Feasibility
//             4. Risk Assessment
//             5. Recommendations
            
//             Important: Preface your response with a clear disclaimer about not constituting legal advice.
//             """.formatted(sanitizedScenario, sanitizedConstraints);
//     }

//     private String validateAndSanitizeInput(String input) {
//         if (input == null) {
//             return "";
//         }
        
//         // Remove any JSON-breaking characters
//         String sanitized = input
//                 .replace("\"", "'")
//                 .replace("\\", "")
//                 .replace("\n", " ")
//                 .replace("\r", " ")
//                 .trim();
                
//         // Basic content validation
//         if (sanitized.length() > 2000) {
//             sanitized = sanitized.substring(0, 2000) + "...";
//         }
        
//         // Check for sensitive legal content
//         if (containsSensitiveLegalTopics(sanitized)) {
//             sanitized = "[LEGAL NOTICE: This scenario contains potentially sensitive legal topics. " +
//                        "Please consult with legal professionals.] " + sanitized;
//         }
        
//         if (sanitized.isEmpty()) {
//             return "";
//         }
        
//         return sanitized;
//     }

//     private boolean containsSensitiveLegalTopics(String input) {
//         List<String> sensitiveTerms = Arrays.asList(
//             "lawsuit", "legal action", "court", "litigation",
//             "criminal", "illegal", "confidential", "classified",
//             "patent", "trademark", "copyright"
//         );
        
//         String lowercaseInput = input.toLowerCase();
//         return sensitiveTerms.stream()
//             .anyMatch(term -> lowercaseInput.contains(term.toLowerCase()));
//     }

//     private String callOllamaApi(String prompt) throws IOException {
//         String jsonBody = "{\"model\":\"mistral\",\"prompt\":\"" + prompt + "\"}";
//         System.out.println("Sending to Ollama: " + jsonBody);
        
//         RequestBody body = RequestBody.create(
//             jsonBody, 
//             MediaType.parse("application/json")
//         );
        
//         Request request = new Request.Builder()
//             .url(apiUrl)
//             .post(body)
//             .build();
            
//         try (Response response = httpClient.newCall(request).execute()) {
//             String responseBody = response.body().string();
//             System.out.println("Ollama response: " + responseBody);
            
//             if (!response.isSuccessful()) {
//                 throw new IOException("API call failed: " + response.code());
//             }
            
//             JsonNode jsonResponse = objectMapper.readTree(responseBody);
//             return jsonResponse.get("response").asText();
//         }
//     }
    
//     private ScenarioAnalysisResponse parseResponse(String response) {
//         // Add legal disclaimer
//         String legalDisclaimer = "IMPORTANT: This analysis is for informational purposes only and does not constitute legal advice. "
//             + "Please consult with qualified legal professionals for specific legal guidance.";
        
//         return ScenarioAnalysisResponse.builder()
//             .scenarioSummary(response)
//             .potentialPitfalls(extractPitfalls(response))
//             .proposedStrategies(extractStrategies(response))
//             .recommendedResources(List.of(
//                 "• Legal consultation recommended",
//                 "• Industry-specific regulations documentation",
//                 "• Project management tools",
//                 "• Development frameworks"
//             ))
//             .disclaimer(legalDisclaimer)
//             .build();
//     }

//     private List<String> extractPitfalls(String response) {
//         // Default pitfalls with emphasis on legal considerations
//         return List.of(
//             "• Legal compliance must be thoroughly evaluated",
//             "• Regulatory requirements need careful consideration",
//             "• Technical feasibility must be assessed",
//             "• Resource allocation needs careful planning"
//         );
//     }

//     private List<String> extractStrategies(String response) {
//         // Default strategies with emphasis on legal compliance
//         return List.of(
//             "• Consult with legal experts before implementation",
//             "• Conduct thorough regulatory compliance review",
//             "• Start with a minimum viable product (MVP)",
//             "• Implement regular compliance checks"
//         );
//     }
    
//     private ScenarioAnalysisResponse getFallbackResponse(String scenario) {
//         return ScenarioAnalysisResponse.builder()
//             .scenarioSummary("Analysis of: " + scenario)
//             .potentialPitfalls(List.of(
//                 "• Technical feasibility must be evaluated",
//                 "• Resource allocation needs careful planning",
//                 "• Timeline considerations are crucial"
//             ))
//             .proposedStrategies(List.of(
//                 "• Start with a minimum viable product (MVP)",
//                 "• Use agile development methodology",
//                 "• Implement regular progress reviews"
//             ))
//             .recommendedResources(List.of(
//                 "• Project management tools",
//                 "• Development frameworks"
//             ))
//             .disclaimer("This is a fallback response due to API issues.")
//             .build();
//     }
// } 


package com.example.scenarioanalyzer.service;

import com.example.scenarioanalyzer.model.ScenarioAnalysisRequest;
import com.example.scenarioanalyzer.model.ScenarioAnalysisResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiService {
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiUrl;
    
    private static final List<String> PROHIBITED_TOPICS = Arrays.asList(
        "how to commit",
        "make a bomb",
        "create weapons",
        "harm someone",
        "illegal drugs",
        "hack into",
        "bypass security",
        "child exploitation",
        "self-harm",
        "suicide",
        "hate speech",
        "discriminate against",
        "violent acts"
    );
    
    private static final List<String> HARMFUL_INDICATORS = Arrays.asList(
        "illegal method",
        "bypass the law",
        "get away with",
        "harm yourself",
        "harm others",
        "dangerous technique",
        "unethical approach"
    );
    
    public AiService(
            OkHttpClient httpClient,
            @Value("${ollama.api.url}") String apiUrl) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
        this.apiUrl = apiUrl;
    }
    
    public ScenarioAnalysisResponse generateAnalysis(ScenarioAnalysisRequest request) {
        try {
            // First validate the input scenario
            String prompt = buildPrompt(request);
            validatePromptSafety(prompt);
            
            // Test if Ollama is responding
            String testResponse = callOllamaApi("Hello, are you working?");
            System.out.println("Test response: " + testResponse);
            
            // If test worked, send actual prompt
            String response = callOllamaApi(prompt);
            return parseResponse(response);
        } catch (IllegalArgumentException e) {
            return getBlockedResponse(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error generating analysis: " + e.getMessage());
            e.printStackTrace();
            return getFallbackResponse(request.getScenario());
        }
    }

    private String buildPrompt(ScenarioAnalysisRequest request) {
        String sanitizedScenario = validateAndSanitizeInput(request.getScenario());
        String sanitizedConstraints = request.getConstraints().stream()
                .map(this::validateAndSanitizeInput)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(", "));

        return """
            [SYSTEM INSTRUCTIONS]
            - You are a professional scenario analysis assistant
            - You must never provide instructions for illegal or harmful activities
            - If a request seems questionable, explain why you can't comply
            - Always emphasize the need for professional consultation
            - Never provide medical, legal or financial advice
            - Flag any potentially harmful suggestions in the input
            
            [TASK]
            Please analyze the following scenario and constraints carefully.
            If you detect any legal implications, highlight them specifically.
            If the request seems questionable, explain why you can't comply.
            
            SCENARIO:
            %s
            
            CONSTRAINTS:
            %s
            
            Please provide your analysis in the following structure:
            1. Scenario Overview
            2. Legal Considerations and Warnings
            3. Technical Feasibility
            4. Risk Assessment
            5. Recommendations
            
            Important: Preface your response with a clear disclaimer about not constituting legal advice.
            """.formatted(sanitizedScenario, sanitizedConstraints);
    }

    private void validatePromptSafety(String prompt) throws IllegalArgumentException {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be empty");
        }
        
        String lowerPrompt = prompt.toLowerCase();
        
        for (String topic : PROHIBITED_TOPICS) {
            if (lowerPrompt.contains(topic)) {
                throw new IllegalArgumentException(
                    "Prompt contains prohibited content related to: " + topic);
            }
        }
        
        // Check for potential jailbreak attempts
        if (lowerPrompt.contains("ignore previous") && 
            (lowerPrompt.contains("instructions") || lowerPrompt.contains("rules"))) {
            throw new IllegalArgumentException(
                "Prompt attempts to override system instructions");
        }
    }

    private String validateAndSanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        
        // Remove any JSON-breaking characters
        String sanitized = input
                .replace("\"", "'")
                .replace("\\", "")
                .replace("\n", " ")
                .replace("\r", " ")
                .trim();
                
        // Basic content validation
        if (sanitized.length() > 2000) {
            sanitized = sanitized.substring(0, 2000) + "...";
        }
        
        // Check for sensitive legal content
        if (containsSensitiveLegalTopics(sanitized)) {
            sanitized = "[LEGAL NOTICE: This scenario contains potentially sensitive legal topics. " +
                       "Please consult with legal professionals.] " + sanitized;
        }
        
        if (sanitized.isEmpty()) {
            return "";
        }
        
        return sanitized;
    }

    private boolean containsSensitiveLegalTopics(String input) {
        List<String> sensitiveTerms = Arrays.asList(
            "lawsuit", "legal action", "court", "litigation",
            "criminal", "illegal", "confidential", "classified",
            "patent", "trademark", "copyright"
        );
        
        String lowercaseInput = input.toLowerCase();
        return sensitiveTerms.stream()
            .anyMatch(term -> lowercaseInput.contains(term.toLowerCase()));
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
    
    private ScenarioAnalysisResponse parseResponse(String response) {
        // Add safety check for the response
        if (containsHarmfulContent(response)) {
            return getBlockedResponse("Response contained harmful content");
        }
        
        // Add legal disclaimer
        String legalDisclaimer = "IMPORTANT: This analysis is for informational purposes only and does not constitute legal advice. "
            + "Please consult with qualified legal professionals for specific legal guidance.";
        
        return ScenarioAnalysisResponse.builder()
            .scenarioSummary(response)
            .potentialPitfalls(extractPitfalls(response))
            .proposedStrategies(extractStrategies(response))
            .recommendedResources(List.of(
                "• Legal consultation recommended",
                "• Industry-specific regulations documentation",
                "• Project management tools",
                "• Development frameworks"
            ))
            .disclaimer(legalDisclaimer)
            .build();
    }

    private boolean containsHarmfulContent(String response) {
        if (response == null) return false;
        
        String lowerResponse = response.toLowerCase();
        return HARMFUL_INDICATORS.stream().anyMatch(lowerResponse::contains);
    }

    private List<String> extractPitfalls(String response) {
        // Default pitfalls with emphasis on legal considerations
        return List.of(
            "• Legal compliance must be thoroughly evaluated",
            "• Regulatory requirements need careful consideration",
            "• Technical feasibility must be assessed",
            "• Resource allocation needs careful planning"
        );
    }

    private List<String> extractStrategies(String response) {
        // Default strategies with emphasis on legal compliance
        return List.of(
            "• Consult with legal experts before implementation",
            "• Conduct thorough regulatory compliance review",
            "• Start with a minimum viable product (MVP)",
            "• Implement regular compliance checks"
        );
    }
    
    private ScenarioAnalysisResponse getBlockedResponse(String reason) {
        return ScenarioAnalysisResponse.builder()
            .scenarioSummary("Request blocked for safety reasons")
            .potentialPitfalls(List.of("Content moderation triggered: " + reason))
            .proposedStrategies(List.of("Please modify your request"))
            .recommendedResources(List.of("Contact support if you believe this was an error"))
            .disclaimer("This request was blocked by our safety filters")
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
            .disclaimer("This is a fallback response due to system issues.")
            .build();
    }
}