package com.hexabiblos.chatbot.bean;

import com.hexabiblos.chatbot.model.ChatMessage;
import com.hexabiblos.chatbot.service.GeminiService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * JSF backing bean for chat state management.
 * Session-scoped to maintain conversation continuity.
 */
@Named
@SessionScoped
public class ChatState implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final int MAX_INPUT_LENGTH = 2000;
    
    @Inject
    private GeminiService geminiService;
    
    private List<ChatMessage> messages;
    private String inputText;
    private boolean loading;
    
    public ChatState() {
        messages = new ArrayList<>();
        inputText = "";
        loading = false;
        
        // Add welcome message
        ChatMessage welcome = new ChatMessage(
            ChatMessage.Role.ASSISTANT,
            "Welcome! I'm your enterprise assistant. How can I help you today?"
        );
        messages.add(welcome);
    }
    
    /**
     * Sends the current input text as a user message and generates a response.
     */
    public void send() {
        if (loading) {
            return; // Prevent concurrent requests
        }
        
        String trimmedInput = (inputText != null) ? inputText.trim() : "";
        
        // Validate input
        if (trimmedInput.isEmpty()) {
            return; // Ignore empty messages
        }
        
        if (trimmedInput.length() > MAX_INPUT_LENGTH) {
            // Add error message
            ChatMessage error = new ChatMessage(
                ChatMessage.Role.SYSTEM,
                "Message too long. Maximum length is " + MAX_INPUT_LENGTH + " characters."
            );
            messages.add(error);
            inputText = "";
            return;
        }
        
        loading = true;
        
        try {
            // Add user message
            ChatMessage userMessage = new ChatMessage(ChatMessage.Role.USER, trimmedInput);
            messages.add(userMessage);
            
            // Generate assistant response
            String responseText = geminiService.generateResponse(messages);
            ChatMessage assistantMessage = new ChatMessage(ChatMessage.Role.ASSISTANT, responseText);
            messages.add(assistantMessage);
            
        } catch (Exception e) {
            // Add error message
            ChatMessage error = new ChatMessage(
                ChatMessage.Role.SYSTEM,
                "An error occurred while processing your message. Please try again."
            );
            messages.add(error);
        } finally {
            loading = false;
            inputText = ""; // Clear input field
        }
    }
    
    /**
     * Clears the chat conversation and resets to initial state.
     */
    public void clear() {
        messages.clear();
        inputText = "";
        loading = false;
        
        // Add welcome message
        ChatMessage welcome = new ChatMessage(
            ChatMessage.Role.ASSISTANT,
            "Chat cleared. How can I help you today?"
        );
        messages.add(welcome);
    }
    
    /**
     * Sends a quick reply message.
     * 
     * @param replyText The quick reply text to send
     */
    public void quickReply(String replyText) {
        if (replyText != null && !replyText.trim().isEmpty()) {
            inputText = replyText.trim();
            send();
        }
    }
    
    // Getters and Setters
    
    public List<ChatMessage> getMessages() {
        return messages;
    }
    
    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
    
    public String getInputText() {
        return inputText;
    }
    
    public void setInputText(String inputText) {
        this.inputText = inputText;
    }
    
    public boolean isLoading() {
        return loading;
    }
    
    public void setLoading(boolean loading) {
        this.loading = loading;
    }
    
    public boolean isDemoMode() {
        return geminiService != null && geminiService.isDemoMode();
    }
}
