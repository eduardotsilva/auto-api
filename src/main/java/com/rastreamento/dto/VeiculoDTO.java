package com.rastreamento.dto;

import com.rastreamento.model.SituacaoVeiculo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para cadastro e atualização de veículos")
public class VeiculoDTO {
    
    @Schema(description = "ID do veículo", example = "1")
    private Long id;
    
    @NotBlank(message = "O IMEI é obrigatório")
    @Pattern(regexp = "^[A-Z0-9]{15}$", message = "IMEI deve conter 15 caracteres alfanuméricos")
    @Schema(description = "IMEI do dispositivo de rastreamento", example = "VEICULO1", required = true)
    private String imei;
    
    @NotBlank(message = "A placa é obrigatória")
    @Pattern(regexp = "^[A-Z]{3}[0-9][0-9A-Z][0-9]{2}$", message = "Placa inválida")
    @Schema(description = "Placa do veículo", example = "ABC1234", required = true)
    private String placa;
    
    @NotBlank(message = "O modelo é obrigatório")
    @Schema(description = "Modelo do veículo", example = "Civic", required = true)
    private String modelo;
    
    @NotBlank(message = "A marca é obrigatória")
    @Schema(description = "Marca do veículo", example = "Honda", required = true)
    private String marca;
    
    @NotBlank(message = "A cor é obrigatória")
    @Schema(description = "Cor do veículo", example = "Preto", required = true)
    private String cor;
    
    @NotNull(message = "O ano é obrigatório")
    @Schema(description = "Ano do veículo", example = "2020", required = true)
    private Integer ano;
    
    @NotBlank(message = "O chassi é obrigatório")
    @Size(min = 17, max = 17, message = "O chassi deve ter 17 caracteres")
    @Schema(description = "Número do chassi do veículo", example = "9BWZZZ377VT004251", required = true)
    private String chassi;
    
    @Schema(description = "ID do rastreador vinculado", example = "1")
    private Long rastreadorId;
    
    @Schema(description = "Situação atual do veículo", example = "RASTREANDO")
    private SituacaoVeiculo situacao;
    
    @Schema(description = "Status de ativação do veículo", example = "true")
    private boolean ativo = true;
    
    @Schema(description = "Última localização conhecida - Data/Hora", example = "2024-03-29T14:30:00")
    private String ultimaLocalizacao;
    
    @Schema(description = "Última localização conhecida - Latitude", example = "-23.550520")
    private Double ultimaLatitude;
    
    @Schema(description = "Última localização conhecida - Longitude", example = "-46.633308")
    private Double ultimaLongitude;
    
    @Schema(description = "Última velocidade registrada em km/h", example = "60.5")
    private Double ultimaVelocidade;
} 