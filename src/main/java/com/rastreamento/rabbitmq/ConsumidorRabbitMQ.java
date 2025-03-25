package com.rastreamento.rabbitmq;

import com.rastreamento.model.DadosLocalizacao;
import com.rastreamento.service.ServicoLocalizacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumidorRabbitMQ {

    private final ServicoLocalizacao servicoLocalizacao;

    @RabbitListener(queues = "fila-tempo-real")
    public void processarLocalizacaoTempoReal(DadosLocalizacao localizacao) {
        log.info("Recebida localização em tempo real: {}", localizacao);
        servicoLocalizacao.enviarLocalizacaoTempoReal(localizacao);
    }

    @RabbitListener(queues = "fila-historico")
    public void processarLocalizacaoHistorico(DadosLocalizacao localizacao) {
        log.info("Recebida localização para histórico: {}", localizacao);
        servicoLocalizacao.salvarLocalizacaoHistorico(localizacao);
    }
} 