package com.rastreamento.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rastreadores")
public class Rastreador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String imei;
    
    @Column(nullable = false)
    private String numero;
    
    @Column(nullable = false)
    private String operadora;
    
    @OneToOne(mappedBy = "rastreador")
    private Veiculo veiculo;
    
    @Column(nullable = false)
    private boolean ativo = true;
    
    @Column(name = "data_ativacao")
    private LocalDateTime dataAtivacao;
    
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;
    
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        ultimaAtualizacao = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        ultimaAtualizacao = LocalDateTime.now();
    }
} 