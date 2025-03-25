package com.rastreamento.controller;

import com.rastreamento.model.DadosLocalizacao;
import com.rastreamento.service.ServicoLocalizacao;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ControladorWebSocket {

    private final ServicoLocalizacao servicoLocalizacao;

    @MessageMapping("/localizacao")
    @SendTo("/topic/localizacoes")
    public DadosLocalizacao processarLocalizacao(DadosLocalizacao localizacao) {
        servicoLocalizacao.salvarLocalizacaoHistorico(localizacao);
        servicoLocalizacao.enviarLocalizacaoTempoReal(localizacao);
        return localizacao;
    }
} 