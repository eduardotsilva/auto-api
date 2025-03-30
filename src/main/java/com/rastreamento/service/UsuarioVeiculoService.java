package com.rastreamento.service;

import com.rastreamento.dto.VeiculoDTO;
import com.rastreamento.converter.VeiculoConverter;
import com.rastreamento.model.Usuario;
import com.rastreamento.model.Veiculo;
import com.rastreamento.repository.UsuarioRepository;
import com.rastreamento.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioVeiculoService {
    
    private final UsuarioRepository usuarioRepository;
    private final VeiculoRepository veiculoRepository;
    private final VeiculoConverter veiculoConverter;
    
    @Transactional(readOnly = true)
    public List<VeiculoDTO> listarVeiculosDoUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        return usuario.getVeiculos().stream()
                .map(veiculoConverter::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void vincularVeiculo(Long usuarioId, Long veiculoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        
        if (usuario.getVeiculos().contains(veiculo)) {
            throw new RuntimeException("Veículo já está vinculado ao usuário");
        }
        
        usuario.getVeiculos().add(veiculo);
        usuarioRepository.save(usuario);
    }
    
    @Transactional
    public void desvincularVeiculo(Long usuarioId, Long veiculoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        
        if (!usuario.getVeiculos().contains(veiculo)) {
            throw new RuntimeException("Veículo não está vinculado ao usuário");
        }
        
        usuario.getVeiculos().remove(veiculo);
        usuarioRepository.save(usuario);
    }
} 