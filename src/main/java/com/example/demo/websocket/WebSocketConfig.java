package com.example.demo.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");      // where the server broadcasts
        config.setApplicationDestinationPrefixes("/app");   // where the client sends
    }


    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ✅ Native WebSocket endpoint (for STOMP + SockJS fallback)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // ✅ CORRECT: allows all origins safely
                .withSockJS();
    }
}
