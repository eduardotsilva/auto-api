package com.rastreamento.dto;

import com.rastreamento.model.SituacaoVeiculo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de resposta para veículos")
public class VeiculoRespostaDTO {
    @Schema(description = "ID do veículo", example = "1")
    private Long id;
    
    @Schema(description = "Placa do veículo", example = "ABC1234")
    private String placa;
    
    @Schema(description = "Marca do veículo", example = "Honda")
    private String marca;
    
    @Schema(description = "Modelo do veículo", example = "Civic")
    private String modelo;
    
    @Schema(description = "Ano do veículo", example = "2020")
    private Integer ano;
    
    @Schema(description = "Cor do veículo", example = "Preto")
    private String cor;
    
    @Schema(description = "Número do chassi do veículo", example = "9BWZZZ377VT004251")
    private String chassi;
    
    @Schema(description = "Número do renavam do veículo", example = "12345678901")
    private String renavam;

    @Schema(description = "Situação atual do veículo", example = "AGUARDANDO_ATIVACAO")
    private SituacaoVeiculo situacao;

    @Schema(description = "Dados do rastreador vinculado")
    private RastreadorRespostaDTO rastreador;
} 