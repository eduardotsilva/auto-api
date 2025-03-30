package com.rastreamento.dto;

import lombok.Data;

@Data
public class VinculoVeiculoDTO {
    
    private Long usuarioId;
    
    private String placa;
    private String modelo;
    private String marca;
    private String cor;
    private Integer ano;
    private String chassi;
    
    private String imei;
    private String numeroSerie;
    private String modeloRastreador;
    private String telefone;
    private String operadora;
} 