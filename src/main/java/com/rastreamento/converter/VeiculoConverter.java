package com.rastreamento.converter;

import com.rastreamento.dto.VeiculoDTO;
import com.rastreamento.model.Veiculo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class VeiculoConverter {
    
    public Veiculo toEntity(VeiculoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setMarca(dto.getMarca());
        veiculo.setCor(dto.getCor());
        veiculo.setAno(dto.getAno());
        veiculo.setChassi(dto.getChassi());
        veiculo.setSituacao(dto.getSituacao());
        veiculo.setAtivo(dto.isAtivo());
        veiculo.setDataCriacao(LocalDateTime.now());
        veiculo.setUltimaAtualizacao(LocalDateTime.now());
        
        return veiculo;
    }
    
    public VeiculoDTO toDTO(Veiculo veiculo) {
        if (veiculo == null) {
            return null;
        }
        
        VeiculoDTO dto = new VeiculoDTO();
        dto.setId(veiculo.getId());
        dto.setPlaca(veiculo.getPlaca());
        dto.setModelo(veiculo.getModelo());
        dto.setMarca(veiculo.getMarca());
        dto.setCor(veiculo.getCor());
        dto.setAno(veiculo.getAno());
        dto.setChassi(veiculo.getChassi());
        
        if (veiculo.getRastreador() != null) {
            dto.setRastreadorId(veiculo.getRastreador().getId());
        }
        
        dto.setSituacao(veiculo.getSituacao());
        dto.setAtivo(veiculo.isAtivo());
        dto.setUltimaLocalizacao(veiculo.getUltimaLocalizacao() != null ? 
            veiculo.getUltimaLocalizacao().toString() : null);
        dto.setUltimaLatitude(veiculo.getUltimaLatitude());
        dto.setUltimaLongitude(veiculo.getUltimaLongitude());
        dto.setUltimaVelocidade(veiculo.getUltimaVelocidade());
        
        return dto;
    }
} 