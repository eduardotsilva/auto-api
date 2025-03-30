package com.rastreamento.controller;

import com.rastreamento.dto.VeiculoDTO;
import com.rastreamento.service.VeiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
@Tag(name = "Veículos", description = "API para gerenciamento de veículos")
public class VeiculoController {
    
    private final VeiculoService veiculoService;
    
    @PostMapping
    @Operation(summary = "Cadastrar novo veículo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VeiculoDTO> cadastrar(@Valid @RequestBody VeiculoDTO dto) {
        return ResponseEntity.ok(veiculoService.cadastrar(dto));
    }
    
    @GetMapping
    @Operation(summary = "Listar veículos com paginação e filtro opcional por usuário")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<VeiculoDTO> listar(
            @RequestParam(required = false) Long usuarioId,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return veiculoService.listar(usuarioId, pageable);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar veículo por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<VeiculoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(veiculoService.buscarPorId(id));
    }
    
        
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veículo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VeiculoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody VeiculoDTO dto) {
        return ResponseEntity.ok(veiculoService.atualizar(id, dto));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir veículo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        veiculoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
} 