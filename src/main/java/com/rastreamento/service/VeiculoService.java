package com.rastreamento.service;

import com.rastreamento.dto.VeiculoDTO;
import com.rastreamento.exception.VeiculoJaCadastradoException;
import com.rastreamento.mapper.VeiculoMapper;
import com.rastreamento.model.Veiculo;
import com.rastreamento.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VeiculoService {
    
    private final VeiculoRepository veiculoRepository;
    private final VeiculoMapper veiculoMapper;
    
    @Transactional
    public VeiculoDTO cadastrar(VeiculoDTO dto) {
        if (veiculoRepository.existsByImei(dto.getImei())) {
            throw new VeiculoJaCadastradoException("IMEI já cadastrado");
        }
        if (veiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new VeiculoJaCadastradoException("Placa já cadastrada");
        }
        if (veiculoRepository.existsByChassi(dto.getChassi())) {
            throw new VeiculoJaCadastradoException("Chassi já cadastrado");
        }
        
        Veiculo veiculo = veiculoMapper.toEntity(dto);
        veiculo = veiculoRepository.save(veiculo);
        return veiculoMapper.toDTO(veiculo);
    }
    
    @Transactional(readOnly = true)
    public List<VeiculoDTO> listarTodos() {
        return veiculoRepository.findAll().stream()
                .map(veiculoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public VeiculoDTO buscarPorId(Long id) {
        return veiculoRepository.findById(id)
                .map(veiculoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
    }
    
    @Transactional(readOnly = true)
    public VeiculoDTO buscarPorImei(String imei) {
        return veiculoRepository.findByImei(imei)
                .map(veiculoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
    }
    
    @Transactional
    public VeiculoDTO atualizar(Long id, VeiculoDTO dto) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        
        if (!veiculo.getImei().equals(dto.getImei()) && veiculoRepository.existsByImei(dto.getImei())) {
            throw new VeiculoJaCadastradoException("IMEI já cadastrado");
        }
        if (!veiculo.getPlaca().equals(dto.getPlaca()) && veiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new VeiculoJaCadastradoException("Placa já cadastrada");
        }
        if (!veiculo.getChassi().equals(dto.getChassi()) && veiculoRepository.existsByChassi(dto.getChassi())) {
            throw new VeiculoJaCadastradoException("Chassi já cadastrado");
        }
        
        veiculo.setImei(dto.getImei());
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setMarca(dto.getMarca());
        veiculo.setCor(dto.getCor());
        veiculo.setAno(dto.getAno());
        veiculo.setChassi(dto.getChassi());
        veiculo.setAtivo(dto.isAtivo());
        
        veiculo = veiculoRepository.save(veiculo);
        return veiculoMapper.toDTO(veiculo);
    }
    
    @Transactional
    public void excluir(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new RuntimeException("Veículo não encontrado");
        }
        veiculoRepository.deleteById(id);
    }
} 