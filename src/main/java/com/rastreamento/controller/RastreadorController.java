package com.rastreamento.controller;

import com.rastreamento.dto.RastreadorDTO;
import com.rastreamento.service.RastreadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rastreadores")
@RequiredArgsConstructor
@Tag(name = "Rastreadores", description = "API para gerenciamento de rastreadores")
public class RastreadorController {
    
    private final RastreadorService rastreadorService;
    
    @PostMapping
    @Operation(summary = "Cadastrar novo rastreador")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RastreadorDTO> cadastrar(@Valid @RequestBody RastreadorDTO dto) {
        return ResponseEntity.ok(rastreadorService.cadastrar(dto));
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os rastreadores")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<List<RastreadorDTO>> listarTodos() {
        return ResponseEntity.ok(rastreadorService.listarTodos());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar rastreador por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<RastreadorDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rastreadorService.buscarPorId(id));
    }
    
    @GetMapping("/imei/{imei}")
    @Operation(summary = "Buscar rastreador por IMEI")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO')")
    public ResponseEntity<RastreadorDTO> buscarPorImei(@PathVariable String imei) {
        return ResponseEntity.ok(rastreadorService.buscarPorImei(imei));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar rastreador")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RastreadorDTO> atualizar(@PathVariable Long id, @Valid @RequestBody RastreadorDTO dto) {
        return ResponseEntity.ok(rastreadorService.atualizar(id, dto));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir rastreador")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        rastreadorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/desvincular")
    @Operation(summary = "Desvincular rastreador do veículo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desvincularVeiculo(@PathVariable Long id) {
        rastreadorService.desvincularVeiculo(id);
        return ResponseEntity.ok().build();
    }
} 