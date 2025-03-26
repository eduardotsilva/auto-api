package com.rastreamento.service;

import com.rastreamento.model.DadosLocalizacao;
import com.rastreamento.repository.DadosLocalizacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicoLocalizacao {

    private final DadosLocalizacaoRepository repositorio;
    private final SimpMessagingTemplate messagingTemplate;
    private final Set<String> veiculosRastreados = ConcurrentHashMap.newKeySet();

    public void enviarLocalizacaoTempoReal(DadosLocalizacao localizacao) {
        log.info("Enviando localização para WebSocket: {}", localizacao);
        messagingTemplate.convertAndSend("/topic/localizacoes", localizacao);
    }

    public void salvarLocalizacaoHistorico(DadosLocalizacao localizacao) {
        log.info("Salvando localização no histórico: {}", localizacao);
        repositorio.save(localizacao);
    }

    public DadosLocalizacao buscarLocalizacaoAtual(String imei) {
        return repositorio.findTop1ByImeiOrderByDataHoraDesc(imei)
                .orElseThrow(() -> new RuntimeException("Rastreador não encontrado: " + imei));
    }

    public List<DadosLocalizacao> buscarHistorico(String imei, int quantidade) {
        return repositorio.findTopByImeiOrderByDataHoraDesc(imei, quantidade);
    }

    public void iniciarRastreamento(String imei) {
        veiculosRastreados.add(imei);
    }

    public void pararRastreamento(String imei) {
        veiculosRastreados.remove(imei);
    }

    public Set<String> listarVeiculosRastreados() {
        return veiculosRastreados;
    }
} 