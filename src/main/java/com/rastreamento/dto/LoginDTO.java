package com.rastreamento.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para autenticação de usuário")
public class LoginDTO {
    
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(description = "Email do usuário", example = "usuario@email.com", required = true)
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    @Schema(description = "Senha do usuário", example = "senha123", required = true, minLength = 6)
    private String senha;
} 