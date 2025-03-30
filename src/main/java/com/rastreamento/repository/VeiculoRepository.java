package com.rastreamento.repository;

import com.rastreamento.model.Veiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    boolean existsByPlaca(String placa);
    boolean existsByChassi(String chassi);
    Page<Veiculo> findByUsuarioId(Long usuarioId, Pageable pageable);
} 