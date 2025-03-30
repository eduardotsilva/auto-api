package com.rastreamento.converter;

import com.rastreamento.dto.RastreadorDTO;
import com.rastreamento.model.Rastreador;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RastreadorConverter {
    
    public Rastreador toEntity(RastreadorDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Rastreador rastreador = new Rastreador();
        rastreador.setImei(dto.getImei());
        rastreador.setNumero(dto.getNumero());
        rastreador.setOperadora(dto.getOperadora());
        rastreador.setAtivo(dto.isAtivo());
        rastreador.setDataCriacao(LocalDateTime.now());
        rastreador.setUltimaAtualizacao(LocalDateTime.now());
        
        return rastreador;
    }
    
    public RastreadorDTO toDTO(Rastreador rastreador) {
        if (rastreador == null) {
            return null;
        }
        
        RastreadorDTO dto = new RastreadorDTO();
        dto.setId(rastreador.getId());
        dto.setImei(rastreador.getImei());
        dto.setNumero(rastreador.getNumero());
        dto.setOperadora(rastreador.getOperadora());
        dto.setVeiculoId(rastreador.getVeiculo() != null ? rastreador.getVeiculo().getId() : null);
        dto.setAtivo(rastreador.isAtivo());
        
        return dto;
    }
} 