package com.rastreamento.controller;

import com.rastreamento.dto.UsuarioDTO;
import com.rastreamento.dto.UsuarioFiltroDTO;
import com.rastreamento.dto.UsuarioRespostaDTO;
import com.rastreamento.dto.VinculoVeiculoDTO;
import com.rastreamento.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "APIs para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(summary = "Criar um novo usuário")
    public ResponseEntity<UsuarioRespostaDTO> criar(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        return ResponseEntity.ok(usuarioService.criar(usuarioDTO));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar usuários com filtros")
    public ResponseEntity<Page<UsuarioRespostaDTO>> buscar(
            UsuarioFiltroDTO filtro,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(usuarioService.buscar(filtro, pageable));
    }

    @PostMapping("/vincular-veiculo")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Void> vincularVeiculo(@RequestBody @Valid VinculoVeiculoDTO vinculoDTO) {
        usuarioService.vincularVeiculo(vinculoDTO);
        return ResponseEntity.ok().build();
    }
} 