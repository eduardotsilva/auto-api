package com.rastreamento.mapper;

import com.rastreamento.dto.UsuarioDTO;
import com.rastreamento.dto.UsuarioRespostaDTO;
import com.rastreamento.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    
    @Mapping(target = "senha", ignore = true)
    Usuario toEntity(UsuarioDTO dto);
    
    UsuarioRespostaDTO toRespostaDTO(Usuario usuario);
} 