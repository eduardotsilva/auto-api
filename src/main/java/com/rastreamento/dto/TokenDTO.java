package com.rastreamento.dto;

import com.rastreamento.model.Usuario.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO com informações do token de autenticação")
public class TokenDTO {
    
    @Schema(description = "Token JWT para autenticação",
           example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
           required = true)
    private String token;
    
    @Schema(description = "Tipo do token",
           example = "Bearer",
           required = true)
    private String type;
    
    @Schema(description = "Tempo de expiração do token em milissegundos",
           example = "86400000",
           required = true)
    private long expiration;
    
    @Schema(description = "Nome do usuário autenticado",
           example = "João da Silva",
           required = true)
    private String username;
    
    @Schema(description = "Papel do usuário no sistema",
           example = "USUARIO",
           required = true,
           allowableValues = {"ADMIN", "USUARIO"})
    private Role role;

    public TokenDTO(String token, String type, long expiration, String username, Role role) {
        this.token = token;
        this.type = type;
        this.expiration = expiration;
        this.username = username;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
} 