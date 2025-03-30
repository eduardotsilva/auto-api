package com.rastreamento.service;

import com.rastreamento.dto.VeiculoDTO;
import com.rastreamento.exception.VeiculoJaCadastradoException;
import com.rastreamento.converter.VeiculoConverter;
import com.rastreamento.model.Veiculo;
import com.rastreamento.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VeiculoService {
    
    private final VeiculoRepository veiculoRepository;
    private final VeiculoConverter veiculoConverter;
    
    @Transactional
    public VeiculoDTO cadastrar(VeiculoDTO dto) {
       
        if (veiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new VeiculoJaCadastradoException("Placa já cadastrada");
        }
        if (veiculoRepository.existsByChassi(dto.getChassi())) {
            throw new VeiculoJaCadastradoException("Chassi já cadastrado");
        }
        
        Veiculo veiculo = veiculoConverter.toEntity(dto);
        veiculo = veiculoRepository.save(veiculo);
        return veiculoConverter.toDTO(veiculo);
    }
    
    @Transactional(readOnly = true)
    public List<VeiculoDTO> listarTodos() {
        return veiculoRepository.findAll().stream()
                .map(veiculoConverter::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public VeiculoDTO buscarPorId(Long id) {
        return veiculoRepository.findById(id)
                .map(veiculoConverter::toDTO)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
    }
    
    @Transactional
    public VeiculoDTO atualizar(Long id, VeiculoDTO dto) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
       
        if (!veiculo.getPlaca().equals(dto.getPlaca()) && veiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new VeiculoJaCadastradoException("Placa já cadastrada");
        }
        if (!veiculo.getChassi().equals(dto.getChassi()) && veiculoRepository.existsByChassi(dto.getChassi())) {
            throw new VeiculoJaCadastradoException("Chassi já cadastrado");
        }
        
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setMarca(dto.getMarca());
        veiculo.setCor(dto.getCor());
        veiculo.setAno(dto.getAno());
        veiculo.setChassi(dto.getChassi());
        veiculo.setAtivo(dto.isAtivo());
        
        veiculo = veiculoRepository.save(veiculo);
        return veiculoConverter.toDTO(veiculo);
    }
    
    @Transactional
    public void excluir(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new RuntimeException("Veículo não encontrado");
        }
        veiculoRepository.deleteById(id);
    }

    @Transactional
    public Page<VeiculoDTO> listar(Long usuarioId, Pageable pageable) {
        if (usuarioId != null) {
            return veiculoRepository.findByUsuarioId(usuarioId, pageable)
                    .map(veiculoConverter::toDTO);
        }
        
        return veiculoRepository.findAll(pageable)
                .map(veiculoConverter::toDTO);
    }
} 