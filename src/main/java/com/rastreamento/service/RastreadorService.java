package com.rastreamento.service;

import com.rastreamento.dto.RastreadorDTO;
import com.rastreamento.exception.RastreadorJaCadastradoException;
import com.rastreamento.converter.RastreadorConverter;
import com.rastreamento.model.Rastreador;
import com.rastreamento.model.Veiculo;
import com.rastreamento.repository.RastreadorRepository;
import com.rastreamento.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RastreadorService {
    
    private final RastreadorRepository rastreadorRepository;
    private final VeiculoRepository veiculoRepository;
    private final RastreadorConverter rastreadorConverter;
    
    @Transactional
    public RastreadorDTO cadastrar(RastreadorDTO dto) {
        if (rastreadorRepository.existsByImei(dto.getImei())) {
            throw new RastreadorJaCadastradoException("IMEI já cadastrado");
        }
        if (rastreadorRepository.existsByNumero(dto.getNumero())) {
            throw new RastreadorJaCadastradoException("Número já cadastrado");
        }
        
        Rastreador rastreador = rastreadorConverter.toEntity(dto);
        
        if (dto.getVeiculoId() != null) {
            Veiculo veiculo = veiculoRepository.findById(dto.getVeiculoId())
                    .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
            
            if (veiculo.getRastreador() != null) {
                throw new RuntimeException("Veículo já possui um rastreador vinculado");
            }
            
            rastreador.setVeiculo(veiculo);
            veiculo.setRastreador(rastreador);
            rastreador.setDataAtivacao(LocalDateTime.now());
        }
        
        rastreador = rastreadorRepository.save(rastreador);
        return rastreadorConverter.toDTO(rastreador);
    }
    
    @Transactional(readOnly = true)
    public List<RastreadorDTO> listarTodos() {
        return rastreadorRepository.findAll().stream()
                .map(rastreadorConverter::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public RastreadorDTO buscarPorId(Long id) {
        return rastreadorRepository.findById(id)
                .map(rastreadorConverter::toDTO)
                .orElseThrow(() -> new RuntimeException("Rastreador não encontrado"));
    }
    
    @Transactional(readOnly = true)
    public RastreadorDTO buscarPorImei(String imei) {
        return rastreadorRepository.findByImei(imei)
                .map(rastreadorConverter::toDTO)
                .orElseThrow(() -> new RuntimeException("Rastreador não encontrado"));
    }
    
    @Transactional
    public RastreadorDTO atualizar(Long id, RastreadorDTO dto) {
        Rastreador rastreador = rastreadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rastreador não encontrado"));
        
        if (!rastreador.getImei().equals(dto.getImei()) && rastreadorRepository.existsByImei(dto.getImei())) {
            throw new RastreadorJaCadastradoException("IMEI já cadastrado");
        }
        if (!rastreador.getNumero().equals(dto.getNumero()) && rastreadorRepository.existsByNumero(dto.getNumero())) {
            throw new RastreadorJaCadastradoException("Número já cadastrado");
        }
        
        // Se está mudando o veículo vinculado
        if (dto.getVeiculoId() != null && !dto.getVeiculoId().equals(rastreador.getVeiculo() != null ? rastreador.getVeiculo().getId() : null)) {
            // Remove vínculo anterior
            if (rastreador.getVeiculo() != null) {
                rastreador.getVeiculo().setRastreador(null);
                rastreador.setVeiculo(null);
            }
            
            // Estabelece novo vínculo
            Veiculo novoVeiculo = veiculoRepository.findById(dto.getVeiculoId())
                    .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
            
            if (novoVeiculo.getRastreador() != null) {
                throw new RuntimeException("Veículo já possui um rastreador vinculado");
            }
            
            rastreador.setVeiculo(novoVeiculo);
            novoVeiculo.setRastreador(rastreador);
            rastreador.setDataAtivacao(LocalDateTime.now());
        }
        
        rastreador.setImei(dto.getImei());
        rastreador.setNumero(dto.getNumero());
        rastreador.setOperadora(dto.getOperadora());
        rastreador.setAtivo(dto.isAtivo());
        
        rastreador = rastreadorRepository.save(rastreador);
        return rastreadorConverter.toDTO(rastreador);
    }
    
    @Transactional
    public void excluir(Long id) {
        Rastreador rastreador = rastreadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rastreador não encontrado"));
        
        if (rastreador.getVeiculo() != null) {
            rastreador.getVeiculo().setRastreador(null);
        }
        
        rastreadorRepository.delete(rastreador);
    }
    
    @Transactional
    public void desvincularVeiculo(Long id) {
        Rastreador rastreador = rastreadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rastreador não encontrado"));
        
        if (rastreador.getVeiculo() != null) {
            rastreador.getVeiculo().setRastreador(null);
            rastreador.setVeiculo(null);
            rastreadorRepository.save(rastreador);
        }
    }
} 