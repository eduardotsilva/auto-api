package com.rastreamento.infrastructure.controller;

import com.rastreamento.domain.model.Localizacao;
import com.rastreamento.domain.service.RastreamentoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/rastreamento")
@RequiredArgsConstructor
public class RastreamentoController {

    private final RastreamentoService rastreamentoService;

    @PostMapping("/dados")
    public ResponseEntity<Void> receberDadosRastreador(@RequestBody String dados) {
        log.info("Recebendo dados do rastreador: {}", dados);
        rastreamentoService.processarDadosRastreador(dados);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/localizacoes/{imei}")
    public ResponseEntity<List<Localizacao>> buscarLocalizacoes(@PathVariable String imei) {
        List<Localizacao> localizacoes = rastreamentoService.buscarUltimasLocalizacoes(imei);
        return ResponseEntity.ok(localizacoes);
    }
} 