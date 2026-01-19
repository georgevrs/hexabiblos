package com.hexabiblos.chatbot.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Domain model representing a chat message in the conversation.
 */
public class ChatMessage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public enum Role {
        USER,
        ASSISTANT,
        SYSTEM
    }
    
    private Role role;
    private String text;
    private Instant timestamp;
    
    public ChatMessage() {
        this.timestamp = Instant.now();
    }
    
    public ChatMessage(Role role, String text) {
        this.role = role;
        this.text = text;
        this.timestamp = Instant.now();
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    
    public boolean isUser() {
        return Role.USER.equals(role);
    }
    
    public boolean isAssistant() {
        return Role.ASSISTANT.equals(role);
    }
    
    public boolean isSystem() {
        return Role.SYSTEM.equals(role);
    }
    
    /**
     * Returns formatted timestamp as string for JSF display.
     * Formats as HH:mm (24-hour format).
     */
    public String getFormattedTimestamp() {
        if (timestamp == null) {
            return "";
        }
        LocalDateTime dateTime = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
