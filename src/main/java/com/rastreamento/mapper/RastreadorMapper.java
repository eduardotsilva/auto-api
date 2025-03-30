package com.rastreamento.mapper;

import com.rastreamento.dto.RastreadorDTO;
import com.rastreamento.model.Rastreador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RastreadorMapper {
    
    RastreadorMapper INSTANCE = Mappers.getMapper(RastreadorMapper.class);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "ultimaAtualizacao", ignore = true)
    @Mapping(target = "dataAtivacao", ignore = true)
    @Mapping(target = "veiculo", ignore = true)
    Rastreador toEntity(RastreadorDTO dto);
    
    @Mapping(target = "veiculoId", source = "veiculo.id")
    RastreadorDTO toDTO(Rastreador entity);
} 