package com.rastreamento.controller;

import com.rastreamento.dto.VeiculoDTO;
import com.rastreamento.service.UsuarioVeiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios/{usuarioId}/veiculos")
@RequiredArgsConstructor
@Tag(name = "Usuários e Veículos", description = "API para gerenciamento da relação entre usuários e veículos")
public class UsuarioVeiculoController {
    
    private final UsuarioVeiculoService usuarioVeiculoService;
    
    @GetMapping
    @Operation(summary = "Listar veículos do usuário")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<VeiculoDTO>> listarVeiculosDoUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(usuarioVeiculoService.listarVeiculosDoUsuario(usuarioId));
    }
    
    @PostMapping("/{veiculoId}")
    @Operation(summary = "Vincular veículo ao usuário")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> vincularVeiculo(@PathVariable Long usuarioId, @PathVariable Long veiculoId) {
        usuarioVeiculoService.vincularVeiculo(usuarioId, veiculoId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{veiculoId}")
    @Operation(summary = "Desvincular veículo do usuário")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desvincularVeiculo(@PathVariable Long usuarioId, @PathVariable Long veiculoId) {
        usuarioVeiculoService.desvincularVeiculo(usuarioId, veiculoId);
        return ResponseEntity.noContent().build();
    }
} 