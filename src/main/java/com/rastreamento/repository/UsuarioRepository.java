package com.rastreamento.repository;

import com.rastreamento.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE " +
           "(:nome IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:ativo IS NULL OR u.ativo = :ativo)")
    Page<Usuario> buscarComFiltros(
        @Param("nome") String nome,
        @Param("email") String email,
        @Param("role") com.rastreamento.model.Role role,
        @Param("ativo") Boolean ativo,
        Pageable pageable
    );
} 