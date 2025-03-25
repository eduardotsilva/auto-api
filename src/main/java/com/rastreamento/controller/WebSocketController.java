package com.rastreamento.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/rastreamento")
    @SendTo("/topic/rastreamento")
    public String rastreamento(String message) {
        return "Mensagem recebida: " + message;
    }
} 