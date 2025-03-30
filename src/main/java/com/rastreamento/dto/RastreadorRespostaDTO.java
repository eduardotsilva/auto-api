package com.rastreamento.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de resposta para rastreadores")
public class RastreadorRespostaDTO {
    @Schema(description = "ID do rastreador", example = "1")
    private Long id;
    
    @Schema(description = "IMEI do dispositivo", example = "123456789012345")
    private String imei;
    
    @Schema(description = "Número de série do dispositivo", example = "SN123456789")
    private String numeroSerie;
    
    @Schema(description = "Modelo do dispositivo", example = "GT06N")
    private String modelo;
    
    @Schema(description = "Telefone do chip do rastreador", example = "11999999999")
    private String telefone;
    
    @Schema(description = "Operadora do chip", example = "VIVO")
    private String operadora;
    
    @Schema(description = "Status de ativação do rastreador", example = "true")
    private boolean ativo;
} 