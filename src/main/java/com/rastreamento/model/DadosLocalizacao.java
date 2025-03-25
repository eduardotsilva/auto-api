package com.rastreamento.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "dados_localizacao")
public class DadosLocalizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String idVeiculo;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double velocidade;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private Boolean ativo = true;
} 