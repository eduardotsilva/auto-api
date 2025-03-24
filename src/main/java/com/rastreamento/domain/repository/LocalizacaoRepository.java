package com.rastreamento.domain.repository;

import com.rastreamento.domain.model.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {
    List<Localizacao> findByImeiOrderByDataHoraDesc(String imei);
} 