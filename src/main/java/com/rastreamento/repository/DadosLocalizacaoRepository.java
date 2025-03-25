package com.rastreamento.repository;

import com.rastreamento.model.DadosLocalizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DadosLocalizacaoRepository extends JpaRepository<DadosLocalizacao, Long> {
    Optional<DadosLocalizacao> findTop1ByIdVeiculoOrderByDataHoraDesc(String idVeiculo);
    
    @Query(value = "SELECT * FROM dados_localizacao WHERE id_veiculo = :idVeiculo ORDER BY data_hora DESC LIMIT :limit", nativeQuery = true)
    List<DadosLocalizacao> findTopByIdVeiculoOrderByDataHoraDesc(@Param("idVeiculo") String idVeiculo, @Param("limit") int limit);
} 