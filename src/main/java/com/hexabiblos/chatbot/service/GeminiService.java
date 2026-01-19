package com.hexabiblos.chatbot.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.hexabiblos.chatbot.model.ChatMessage;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for integrating with Google Gemini AI API.
 * Handles API key configuration, conversation building, and response generation.
 */
@Named
@ApplicationScoped
public class GeminiService {
    
    private static final String DEFAULT_MODEL = "gemini-3-flash-preview";
    private static final int DEFAULT_MAX_TURNS = 20;
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);
    
    private static final String SYSTEM_INSTRUCTION = 
        "You are a professional, concise AI assistant for public-sector and enterprise use. " +
        "Provide clear, helpful, and accurate responses. Keep responses concise and focused.";
    
    private static final String DEMO_MODE_RESPONSE = 
        "Gemini API key not configured. Running in demo mode.";
    
    private Client client;
    private String apiKey;
    private String modelName;
    private int maxTurns;
    private boolean demoMode;
    
    @PostConstruct
    public void init() {
        // Try to load .env file first, then fall back to environment variables
        Dotenv dotenv = null;
        
        // Try to find .env file in project root
        // Strategy: Start from current directory and traverse up to find pom.xml (project root)
        java.io.File projectRoot = null;
        java.io.File currentDir = new java.io.File(System.getProperty("user.dir"));
        
        // Traverse up directories looking for pom.xml (indicates project root)
        for (int i = 0; i < 10 && currentDir != null; i++) {
            java.io.File pomFile = new java.io.File(currentDir, "pom.xml");
            if (pomFile.exists() && pomFile.isFile()) {
                projectRoot = currentDir;
                System.out.println("DEBUG: Found project root: " + projectRoot.getAbsolutePath());
                break;
            }
            currentDir = currentDir.getParentFile();
        }
        
        // Try .env file locations in priority order
        java.util.List<java.io.File> envFileCandidates = new java.util.ArrayList<>();
        
        // 1. Project root (where pom.xml is)
        if (projectRoot != null) {
            envFileCandidates.add(new java.io.File(projectRoot, ".env"));
        }
        
        // 2. Explicit known path (for this specific project)
        envFileCandidates.add(new java.io.File("C:\\Users\\georg\\Desktop\\Interviews\\Hexabiblos\\.env"));
        
        // 3. User home
        String userHome = System.getProperty("user.home");
        if (userHome != null) {
            envFileCandidates.add(new java.io.File(userHome, ".env"));
        }
        
        // 4. WildFly/Tomcat base
        String jbossBase = System.getProperty("jboss.server.base.dir");
        if (jbossBase != null) {
            envFileCandidates.add(new java.io.File(jbossBase, ".env"));
        }
        
        // Try each candidate
        for (java.io.File envFile : envFileCandidates) {
            try {
                if (envFile.exists() && envFile.isFile() && envFile.canRead()) {
                    dotenv = Dotenv.configure()
                        .directory(envFile.getParent())
                        .filename(".env")
                        .ignoreIfMissing()
                        .load();
                    System.out.println("✓ Loaded .env file from: " + envFile.getAbsolutePath());
                    break;
                }
            } catch (Exception e) {
                // Try next candidate
                continue;
            }
        }
        
        // If still not found, try default location
        if (dotenv == null) {
            try {
                dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            } catch (DotenvException e) {
                System.out.println("⚠ No .env file found in standard locations, using environment variables");
            }
        }
        
        // Get API key: .env file -> GEMINI_API_KEY env var -> GOOGLE_API_KEY env var
        apiKey = getEnvValue(dotenv, "GEMINI_API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            apiKey = getEnvValue(dotenv, "GOOGLE_API_KEY");
        }
        
        // Debug: Log what we found
        if (dotenv != null) {
            String envKey = dotenv.get("GEMINI_API_KEY");
            System.out.println("DEBUG: .env file loaded. GEMINI_API_KEY from .env: " + 
                (envKey != null && !envKey.trim().isEmpty() ? "FOUND (length: " + envKey.length() + ")" : "NOT FOUND"));
        }
        System.out.println("DEBUG: Final API key: " + 
            (apiKey != null && !apiKey.trim().isEmpty() ? "FOUND (length: " + apiKey.length() + ")" : "NOT FOUND"));
        System.out.println("DEBUG: Current working directory: " + System.getProperty("user.dir"));
        
        // Get model name
        modelName = getEnvValue(dotenv, "GEMINI_MODEL");
        if (modelName == null || modelName.trim().isEmpty()) {
            modelName = DEFAULT_MODEL;
        }
        
        // Get max turns
        String maxTurnsStr = getEnvValue(dotenv, "GEMINI_MAX_TURNS");
        if (maxTurnsStr != null && !maxTurnsStr.trim().isEmpty()) {
            try {
                maxTurns = Integer.parseInt(maxTurnsStr.trim());
            } catch (NumberFormatException e) {
                maxTurns = DEFAULT_MAX_TURNS;
            }
        } else {
            maxTurns = DEFAULT_MAX_TURNS;
        }
        
        demoMode = (apiKey == null || apiKey.trim().isEmpty());
        
        // Log configuration status
        if (demoMode) {
            System.out.println("WARNING: Gemini API key not found. Running in DEMO MODE.");
            System.out.println("Checked .env file and environment variables: GEMINI_API_KEY, GOOGLE_API_KEY");
        } else {
            System.out.println("Gemini API key found. Initializing client...");
            System.out.println("Model: " + modelName);
            System.out.println("Max turns: " + maxTurns);
        }
        
        if (!demoMode) {
            try {
                // Initialize client with API key directly (preferred method)
                // This avoids needing to modify environment variables at runtime
                client = Client.builder()
                    .apiKey(apiKey)
                    .build();
                System.out.println("✓ Gemini client initialized successfully with API key");
            } catch (Exception e) {
                System.err.println("Failed to initialize Gemini client: " + e.getMessage());
                e.printStackTrace();
                demoMode = true;
            }
        }
    }
    
    /**
     * Gets a value from .env file first, then falls back to environment variable.
     */
    private String getEnvValue(Dotenv dotenv, String key) {
        // Check environment variable FIRST (set by run script)
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.trim().isEmpty()) {
            System.out.println("DEBUG: Found " + key + " in environment variable");
            return envValue.trim();
        }
        
        // Fall back to .env file
        if (dotenv != null) {
            try {
                String value = dotenv.get(key);
                if (value != null && !value.trim().isEmpty()) {
                    System.out.println("DEBUG: Found " + key + " in .env file");
                    return value.trim();
                }
            } catch (Exception e) {
                // Key not in .env file
            }
        }
        
        return null;
    }
    
    /**
     * Generates an AI response based on the conversation history.
     * 
     * @param messages Conversation history (last N messages)
     * @return Response text from Gemini or demo mode message
     */
    public String generateResponse(List<ChatMessage> messages) {
        if (demoMode) {
            return DEMO_MODE_RESPONSE;
        }
        
        if (messages == null || messages.isEmpty()) {
            return "I'm ready to help. What would you like to know?";
        }
        
        try {
            // Build prompt from conversation history
            String prompt = buildPrompt(messages);
            
            // Generate response with timeout protection
            Instant startTime = Instant.now();
            
            // Use the SDK API: client.models.generateContent(modelName, promptText, config)
            GenerateContentResponse response = client.models.generateContent(
                modelName,
                prompt,
                null  // No config for now
            );
            
            Duration elapsed = Duration.between(startTime, Instant.now());
            if (elapsed.compareTo(REQUEST_TIMEOUT) > 0) {
                return "Request timed out. Please try again.";
            }
            
            // Extract response text - handle SDK version variations
            if (response != null) {
                String responseText = extractTextFromResponse(response);
                if (responseText != null && !responseText.trim().isEmpty()) {
                    return responseText.trim();
                }
            }
            
            return "Received empty response from Gemini. Please try again.";
            
        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("network") || 
                e.getMessage() != null && e.getMessage().contains("connection")) {
                return "Network error occurred. Please check your connection and try again.";
            }
            return "An error occurred while processing your request. Please try again.";
        }
    }
    
    /**
     * Builds a prompt string from conversation history.
     * Includes system instruction and formatted conversation turns.
     */
    private String buildPrompt(List<ChatMessage> messages) {
        StringBuilder prompt = new StringBuilder();
        
        // Add system instruction
        prompt.append(SYSTEM_INSTRUCTION).append("\n\n");
        prompt.append("Conversation history:\n");
        
        // Get last N messages (respecting maxTurns)
        List<ChatMessage> recentMessages = messages.stream()
            .filter(m -> !m.isSystem())
            .collect(Collectors.toList());
        
        int startIndex = Math.max(0, recentMessages.size() - maxTurns);
        List<ChatMessage> contextMessages = recentMessages.subList(startIndex, recentMessages.size());
        
        // Format conversation
        for (ChatMessage msg : contextMessages) {
            if (msg.isUser()) {
                prompt.append("User: ").append(msg.getText()).append("\n");
            } else if (msg.isAssistant()) {
                prompt.append("Assistant: ").append(msg.getText()).append("\n");
            }
        }
        
        prompt.append("\nPlease provide a helpful response to the user's last message.");
        
        return prompt.toString();
    }
    
    public boolean isDemoMode() {
        return demoMode;
    }
    
    public String getModelName() {
        return modelName;
    }
    
    /**
     * Extracts text from Gemini API response.
     * Handles SDK version variations where text() may return String or Optional<String>.
     */
    private String extractTextFromResponse(GenerateContentResponse response) {
        if (response == null) {
            return "";
        }

        // 1) Try convenience helper - in some versions returns String directly
        try {
            String text = response.text();
            if (text != null && !text.isBlank()) {
                return text;
            }
        } catch (Exception ignored) {
            // Method may not exist or behave differently in some versions
        }

        // 2) Fallback: candidates -> content -> parts -> text
        try {
            var candidatesOpt = response.candidates(); // Optional<List<Candidate>>
            var candidates = candidatesOpt.orElse(Collections.emptyList());
            if (candidates.isEmpty()) {
                return "";
            }

            var first = candidates.get(0);
            var contentOpt = first.content(); // Optional<Content>
            var content = contentOpt.orElse(null);
            if (content == null) {
                return "";
            }

            // parts() may return Optional<List<Part>> or List<Part> - handle both
            Object partsResult = content.parts();
            List<?> parts;
            
            if (partsResult instanceof java.util.Optional) {
                // It's Optional<List<Part>>
                @SuppressWarnings("unchecked")
                java.util.Optional<List<?>> partsOpt = (java.util.Optional<List<?>>) partsResult;
                parts = partsOpt.orElse(Collections.emptyList());
            } else if (partsResult instanceof List) {
                // It's List<Part> directly
                parts = (List<?>) partsResult;
            } else {
                return "";
            }
            
            if (parts == null || parts.isEmpty()) {
                return "";
            }

            // Extract text from first part
            Object partObj = parts.get(0);
            
            // Use reflection to call text() method since part is Object
            try {
                var textMethod = partObj.getClass().getMethod("text");
                Object result = textMethod.invoke(partObj);
                
                // Handle Optional<String> return type
                if (result instanceof java.util.Optional) {
                    @SuppressWarnings("unchecked")
                    java.util.Optional<String> textOpt = (java.util.Optional<String>) result;
                    return textOpt.orElse("");
                }
                // Handle String return type
                else if (result instanceof String) {
                    return (String) result;
                }
                return "";
            } catch (Exception e) {
                System.err.println("Error extracting text from part: " + e.getMessage());
                return "";
            }
        } catch (Exception e) {
            System.err.println("Error extracting text from response: " + e.getMessage());
            return "";
        }
    }
}
