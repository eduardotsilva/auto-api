package com.rastreamento.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RastreadorDTO {

    private Long id;

    @NotBlank(message = "O IMEI é obrigatório")
    private String imei;

    @NotBlank(message = "O número é obrigatório")
    private String numero;

    @NotBlank(message = "A operadora é obrigatória")
    private String operadora;

    private Long veiculoId;

    private boolean ativo;
} 