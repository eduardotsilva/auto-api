package com.rastreamento.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "localizacoes")
public class Localizacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imei;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double velocidade;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column
    private String direcao;

    @Column
    private Integer satelites;

    @Column
    private String status;

    @PrePersist
    protected void onCreate() {
        dataHora = LocalDateTime.now();
    }
} 