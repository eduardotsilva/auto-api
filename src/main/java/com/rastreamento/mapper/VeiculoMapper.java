package com.rastreamento.mapper;

import com.rastreamento.dto.VeiculoDTO;
import com.rastreamento.model.Veiculo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VeiculoMapper {
    
    VeiculoMapper INSTANCE = Mappers.getMapper(VeiculoMapper.class);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "ultimaAtualizacao", ignore = true)
    @Mapping(target = "ultimaLocalizacao", ignore = true)
    @Mapping(target = "ultimaLatitude", ignore = true)
    @Mapping(target = "ultimaLongitude", ignore = true)
    @Mapping(target = "ultimaVelocidade", ignore = true)
    @Mapping(target = "rastreador", ignore = true)
    Veiculo toEntity(VeiculoDTO dto);
    
    @Mapping(target = "rastreadorId", source = "rastreador.id")
    VeiculoDTO toDTO(Veiculo entity);
} 