package com.rastreamento.controller;

import com.rastreamento.model.DadosLocalizacao;
import com.rastreamento.service.ServicoLocalizacao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/rastreadores")
@RequiredArgsConstructor
public class RastreadorController {

    private final ServicoLocalizacao servicoLocalizacao;

    @GetMapping("/{imei}/localizacao")
    public ResponseEntity<DadosLocalizacao> buscarLocalizacaoAtual(@PathVariable String imei) {
        DadosLocalizacao localizacao = servicoLocalizacao.buscarLocalizacaoAtual(imei);
        return ResponseEntity.ok(localizacao);
    }

    @GetMapping("/{imei}/historico")
    public ResponseEntity<List<DadosLocalizacao>> buscarHistorico(
            @PathVariable String imei,
            @RequestParam(defaultValue = "10") int quantidade) {
        List<DadosLocalizacao> historico = servicoLocalizacao.buscarHistorico(imei, quantidade);
        return ResponseEntity.ok(historico);
    }

    @PostMapping("/{imei}/rastrear")
    public ResponseEntity<Void> iniciarRastreamento(@PathVariable String imei) {
        servicoLocalizacao.iniciarRastreamento(imei);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{imei}/rastrear")
    public ResponseEntity<Void> pararRastreamento(@PathVariable String imei) {
        servicoLocalizacao.pararRastreamento(imei);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rastreando")
    public ResponseEntity<Set<String>> listarVeiculosRastreados() {
        return ResponseEntity.ok(servicoLocalizacao.listarVeiculosRastreados());
    }
} 