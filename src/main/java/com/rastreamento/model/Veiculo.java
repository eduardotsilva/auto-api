package com.rastreamento.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "veiculos")
public class Veiculo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
       
    @Column(nullable = false)
    private String placa;
    
    @Column(nullable = false)
    private String modelo;
    
    @Column(nullable = false)
    private String marca;
    
    @Column(nullable = false)
    private String cor;
    
    @Column(nullable = false)
    private Integer ano;
    
    @Column(nullable = false)
    private String chassi;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rastreador_id", nullable = true)
    private Rastreador rastreador;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;
    
    @Enumerated(EnumType.STRING)
    private SituacaoVeiculo situacao = SituacaoVeiculo.DESLIGADO;
    
    @Column(nullable = false)
    private boolean ativo = true;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;
    
    @Column(name = "ultima_localizacao")
    private LocalDateTime ultimaLocalizacao;
    
    @Column(name = "ultima_latitude")
    private Double ultimaLatitude;
    
    @Column(name = "ultima_longitude")
    private Double ultimaLongitude;
    
    @Column(name = "ultima_velocidade")
    private Double ultimaVelocidade;
    
    @PrePersist
    protected void onCreate() {
    	ativo = true;
    	situacao = SituacaoVeiculo.DESLIGADO;
        dataCriacao = LocalDateTime.now();
        ultimaAtualizacao = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        ultimaAtualizacao = LocalDateTime.now();
    }
} 