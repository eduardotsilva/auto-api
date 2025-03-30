package com.rastreamento.mapper;

import com.rastreamento.dto.UsuarioDTO;
import com.rastreamento.dto.UsuarioRespostaDTO;
import com.rastreamento.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", constant = "true")
    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "ultimoAcesso", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "veiculos", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Usuario toEntity(UsuarioDTO dto);
    
    UsuarioRespostaDTO toRespostaDTO(Usuario usuario);
} 