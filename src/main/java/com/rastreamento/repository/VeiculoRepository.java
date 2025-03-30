package com.rastreamento.repository;

import com.rastreamento.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    Optional<Veiculo> findByImei(String imei);
    boolean existsByImei(String imei);
    boolean existsByPlaca(String placa);
    boolean existsByChassi(String chassi);
} 