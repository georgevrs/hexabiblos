/**
 * Enterprise Chatbot - Client-side Chat Logic
 * Stage 1: Pure client-side simulation, no backend calls
 */

(function() {
    'use strict';

    // Chat state
    const chatState = {
        messages: [],
        isTyping: false
    };

    // Canned bot responses based on keyword matching
    const botResponses = {
        help: {
            title: "Help Information",
            content: "I'm here to assist you with various tasks. You can ask me about:\n\n" +
                    "• General information and questions\n" +
                    "• Feature demonstrations\n" +
                    "• Navigation assistance\n\n" +
                    "Try asking about 'features' to see what I can do, or just ask me anything!"
        },
        features: {
            title: "Available Features",
            content: "Here are the features I can help you with:\n\n" +
                    "✓ Real-time chat interface\n" +
                    "✓ Quick reply suggestions\n" +
                    "✓ Typing indicators\n" +
                    "✓ Message history\n" +
                    "✓ Responsive design\n" +
                    "✓ Enterprise-grade UI\n\n" +
                    "This is Stage 1 of the chatbot - a fully functional UI with simulated responses."
        },
        reset: {
            title: "Chat Reset",
            content: "The chat has been reset. How can I help you today?"
        },
        default: {
            title: "Response",
            content: "Thank you for your message. I understand you're looking for assistance. " +
                    "In Stage 1, I'm simulating responses. For more specific help, try asking about 'help' or 'features'."
        }
    };

    /**
     * Initialize chat on page load
     */
    function initChat() {
        console.log('Chat initialized');
        
        // Focus on textarea
        const textarea = document.getElementById('messageInput');
        if (textarea) {
            textarea.focus();
        }

        // Add welcome message to state
        chatState.messages.push({
            type: 'bot',
            content: "Welcome! I'm your enterprise assistant. How can I help you today?",
            timestamp: new Date()
        });
    }

    /**
     * Get current timestamp as formatted string
     */
    function getTimestamp() {
        const now = new Date();
        const hours = now.getHours().toString().padStart(2, '0');
        const minutes = now.getMinutes().toString().padStart(2, '0');
        return `${hours}:${minutes}`;
    }

    /**
     * Generate bot response based on user message
     */
    function generateBotResponse(userMessage) {
        const lowerMessage = userMessage.toLowerCase().trim();
        
        if (lowerMessage.includes('help')) {
            return botResponses.help;
        } else if (lowerMessage.includes('feature')) {
            return botResponses.features;
        } else if (lowerMessage.includes('reset')) {
            return botResponses.reset;
        } else {
            return botResponses.default;
        }
    }

    /**
     * Add message to chat
     */
    function addMessage(type, content) {
        const messagesContainer = document.getElementById('chatMessages');
        if (!messagesContainer) return;

        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${type}-message`;
        
        const contentDiv = document.createElement('div');
        contentDiv.className = 'message-content';
        
        const p = document.createElement('p');
        p.textContent = content;
        contentDiv.appendChild(p);
        
        const timestampDiv = document.createElement('div');
        timestampDiv.className = 'message-timestamp';
        timestampDiv.textContent = getTimestamp();
        
        messageDiv.appendChild(contentDiv);
        messageDiv.appendChild(timestampDiv);
        messagesContainer.appendChild(messageDiv);

        // Update state
        chatState.messages.push({
            type: type,
            content: content,
            timestamp: new Date()
        });

        // Scroll to bottom
        scrollToBottom();
    }

    /**
     * Show typing indicator
     */
    function showTypingIndicator() {
        const indicator = document.getElementById('typingIndicator');
        if (indicator) {
            indicator.style.display = 'block';
            chatState.isTyping = true;
            scrollToBottom();
        }
    }

    /**
     * Hide typing indicator
     */
    function hideTypingIndicator() {
        const indicator = document.getElementById('typingIndicator');
        if (indicator) {
            indicator.style.display = 'none';
            chatState.isTyping = false;
        }
    }

    /**
     * Scroll chat to bottom
     */
    function scrollToBottom() {
        const messagesContainer = document.getElementById('chatMessages');
        if (messagesContainer) {
            setTimeout(() => {
                messagesContainer.scrollTop = messagesContainer.scrollHeight;
            }, 100);
        }
    }

    /**
     * Send user message
     */
    function sendMessage() {
        const textarea = document.getElementById('messageInput');
        if (!textarea) return;

        const message = textarea.value.trim();
        
        // Validate: ignore empty messages
        if (!message) {
            return;
        }

        // Add user message
        addMessage('user', message);

        // Clear textarea
        textarea.value = '';
        textarea.style.height = 'auto';

        // Show typing indicator
        showTypingIndicator();

        // Generate and show bot response after delay (600-1200ms)
        const delay = 600 + Math.random() * 600; // Random between 600-1200ms
        
        setTimeout(() => {
            hideTypingIndicator();
            const botResponse = generateBotResponse(message);
            addMessage('bot', botResponse.content);
        }, delay);
    }

    /**
     * Handle keyboard events in textarea
     */
    function handleKeyDown(event) {
        // Enter key sends message (unless Shift is held)
        if (event.key === 'Enter' && !event.shiftKey) {
            event.preventDefault();
            sendMessage();
        }
        // Shift+Enter allows newline (default behavior)
    }

    /**
     * Send quick reply
     */
    function sendQuickReply(replyText) {
        const textarea = document.getElementById('messageInput');
        if (textarea) {
            textarea.value = replyText;
            sendMessage();
        }
    }

    /**
     * Clear chat conversation
     */
    function clearChat() {
        const messagesContainer = document.getElementById('chatMessages');
        if (!messagesContainer) return;

        // Clear DOM (keep welcome message structure)
        const welcomeMessage = messagesContainer.querySelector('.welcome-message');
        messagesContainer.innerHTML = '';
        if (welcomeMessage) {
            messagesContainer.appendChild(welcomeMessage);
        }

        // Reset state
        chatState.messages = [];
        chatState.isTyping = false;
        hideTypingIndicator();

        // Add reset confirmation
        setTimeout(() => {
            addMessage('bot', botResponses.reset.content);
        }, 300);
    }

    // Expose functions to global scope for JSF onclick handlers
    window.sendMessage = sendMessage;
    window.handleKeyDown = handleKeyDown;
    window.sendQuickReply = sendQuickReply;
    window.clearChat = clearChat;

    // Initialize when DOM is ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initChat);
    } else {
        initChat();
    }

})();
