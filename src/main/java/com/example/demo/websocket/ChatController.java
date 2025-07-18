package com.example.demo.websocket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.demo.model.ChatMessage;

@Controller
public class ChatController {

                @MessageMapping("/typing")
            @SendTo("/topic/typing")
            public String typing(String username) {
                return username;
            }


    @MessageMapping("/chat") // Client sends to /app/chat
    @SendTo("/topic/messages") // Server broadcasts to /topic/messages
    public ChatMessage send(ChatMessage message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        message.setTimestamp(timestamp);
        
        System.out.println("🔥 Message received from: " + message.getSender() + " - " + message.getContent());
        return message;
    }
}
