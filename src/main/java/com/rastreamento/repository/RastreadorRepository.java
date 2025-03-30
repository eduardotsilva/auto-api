package com.rastreamento.repository;

import com.rastreamento.model.Rastreador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RastreadorRepository extends JpaRepository<Rastreador, Long> {
    Optional<Rastreador> findByImei(String imei);
    boolean existsByImei(String imei);
    boolean existsByNumero(String numero);
} 