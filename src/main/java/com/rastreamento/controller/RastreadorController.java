package com.rastreamento.controller;

import com.rastreamento.model.DadosLocalizacao;
import com.rastreamento.service.ServicoLocalizacao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/rastreadores")
@RequiredArgsConstructor
@Tag(name = "Rastreamento", description = "Endpoints para gerenciamento de rastreadores")
@SecurityRequirement(name = "Bearer Authentication")
public class RastreadorController {

    private final ServicoLocalizacao servicoLocalizacao;

    @Operation(summary = "Buscar localização atual", description = "Retorna a localização atual de um rastreador específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Localização encontrada",
                content = @Content(schema = @Schema(implementation = DadosLocalizacao.class))),
        @ApiResponse(responseCode = "404", description = "Rastreador não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{imei}/localizacao")
    public ResponseEntity<DadosLocalizacao> buscarLocalizacaoAtual(
            @Parameter(description = "IMEI do rastreador", required = true)
            @PathVariable String imei) {
        DadosLocalizacao localizacao = servicoLocalizacao.buscarLocalizacaoAtual(imei);
        return ResponseEntity.ok(localizacao);
    }

    @Operation(summary = "Buscar histórico de localizações", description = "Retorna o histórico de localizações de um rastreador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico encontrado"),
        @ApiResponse(responseCode = "404", description = "Rastreador não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{imei}/historico")
    public ResponseEntity<List<DadosLocalizacao>> buscarHistorico(
            @Parameter(description = "IMEI do rastreador", required = true)
            @PathVariable String imei,
            @Parameter(description = "Quantidade de registros a retornar", example = "10")
            @RequestParam(defaultValue = "10") int quantidade) {
        List<DadosLocalizacao> historico = servicoLocalizacao.buscarHistorico(imei, quantidade);
        return ResponseEntity.ok(historico);
    }

    @Operation(summary = "Iniciar rastreamento", description = "Inicia o rastreamento de um veículo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rastreamento iniciado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Rastreador não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/{imei}/rastrear")
    public ResponseEntity<Void> iniciarRastreamento(
            @Parameter(description = "IMEI do rastreador", required = true)
            @PathVariable String imei) {
        servicoLocalizacao.iniciarRastreamento(imei);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Parar rastreamento", description = "Para o rastreamento de um veículo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rastreamento parado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Rastreador não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{imei}/rastrear")
    public ResponseEntity<Void> pararRastreamento(
            @Parameter(description = "IMEI do rastreador", required = true)
            @PathVariable String imei) {
        servicoLocalizacao.pararRastreamento(imei);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar veículos rastreados", description = "Retorna a lista de IMEIs dos veículos sendo rastreados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autorizado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/rastreando")
    public ResponseEntity<Set<String>> listarVeiculosRastreados() {
        return ResponseEntity.ok(servicoLocalizacao.listarVeiculosRastreados());
    }
} 