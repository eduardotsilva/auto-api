package com.rastreamento.repository;

import com.rastreamento.model.Usuario;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecification {
    
    public static Specification<Usuario> comNome(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isEmpty()) {
                return null;
            }
            return cb.like(root.get("nome"), "%" + nome + "%");
        };
    }

    public static Specification<Usuario> comEmail(String email) {
        return (root, query, cb) -> {
            if (email == null || email.isEmpty()) {
                return null;
            }
            return cb.like(root.get("email"), "%" + email + "%");
        };
    }

    public static Specification<Usuario> comRole(String role) {
        return (root, query, cb) -> {
            if (role == null) {
                return null;
            }
            return cb.equal(root.get("role"), role);
        };
    }

    public static Specification<Usuario> comAtivo(Boolean ativo) {
        return (root, query, cb) -> {
            if (ativo == null) {
                return null;
            }
            return cb.equal(root.get("ativo"), ativo);
        };
    }
} 