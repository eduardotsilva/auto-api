package com.rastreamento.dto;

import com.rastreamento.model.Usuario.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO com informações do usuário após criação/atualização")
public class UsuarioRespostaDTO {
    
    @Schema(description = "ID do usuário",
           example = "1",
           required = true)
    private Long id;
    
    @Schema(description = "Nome completo do usuário",
           example = "João da Silva",
           required = true)
    private String nome;
    
    @Schema(description = "Email do usuário",
           example = "joao.silva@email.com",
           required = true)
    private String email;
    
    @Schema(description = "Papel do usuário no sistema",
           example = "USUARIO",
           required = true,
           allowableValues = {"ADMIN", "USUARIO"})
    private Role role;
    
    @Schema(description = "Status do usuário no sistema",
           example = "true",
           required = true)
    private boolean ativo;
    
    @Schema(description = "Data e hora de criação do usuário",
           example = "2024-03-26T10:30:00",
           required = true)
    private LocalDateTime dataCriacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
} 