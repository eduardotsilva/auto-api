package com.rastreamento.dto;

import com.rastreamento.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para criação e atualização de usuário")
public class UsuarioDTO {
    
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Schema(description = "Nome completo do usuário", 
           example = "João da Silva", 
           required = true, 
           minLength = 3, 
           maxLength = 100)
    private String nome;
    
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(description = "Email do usuário (será usado para login)", 
           example = "joao.silva@email.com", 
           required = true)
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Senha do usuário (mínimo 6 caracteres)", 
           example = "senha123", 
           required = true, 
           minLength = 6,
           format = "password")
    private String senha;
    
    @NotNull(message = "A role é obrigatória")
    @Schema(description = "Papel do usuário no sistema", 
           example = "USUARIO", 
           required = true,
           allowableValues = {"ADMIN", "USUARIO"})
    private Role role;
} 