package com.rastreamento.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "localizacoes")
@Schema(description = "Dados de localização de um rastreador")
public class DadosLocalizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do registro de localização",
           example = "1",
           accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "IMEI do rastreador",
           example = "123456789012345",
           required = true)
    private String imei;

    @Schema(description = "Latitude da localização",
           example = "-23.550520",
           required = true)
    private Double latitude;

    @Schema(description = "Longitude da localização",
           example = "-46.633308",
           required = true)
    private Double longitude;

    @Schema(description = "Velocidade em km/h",
           example = "60.5",
           required = true)
    private Double velocidade;

    @Schema(description = "Direção em graus (0-360)",
           example = "180.0",
           required = true)
    private String direcao;

    @Schema(description = "Status do sinal GPS (A=válido, V=inválido)",
           example = "A",
           required = true,
           allowableValues = {"A", "V"})
    private String status;

    @Schema(description = "Data e hora da localização",
           example = "2024-03-26T10:30:00",
           required = true)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private Boolean ativo = true;
} 