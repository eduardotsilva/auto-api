package com.rastreamento.service;

import com.rastreamento.dto.UsuarioDTO;
import com.rastreamento.dto.UsuarioFiltroDTO;
import com.rastreamento.dto.UsuarioRespostaDTO;
import com.rastreamento.exception.EmailJaCadastradoException;
import com.rastreamento.mapper.UsuarioMapper;
import com.rastreamento.model.Usuario;
import com.rastreamento.repository.UsuarioRepository;
import com.rastreamento.repository.UsuarioSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioRespostaDTO criar(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuario.setAtivo(true);

        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toRespostaDTO(usuario);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioRespostaDTO> buscar(UsuarioFiltroDTO filtro, Pageable pageable) {
        Specification<Usuario> spec = Specification.where(null);
        
        if (filtro.getNome() != null) {
            spec = spec.and(UsuarioSpecification.comNome(filtro.getNome()));
        }
        if (filtro.getEmail() != null) {
            spec = spec.and(UsuarioSpecification.comEmail(filtro.getEmail()));
        }
        if (filtro.getRole() != null) {
            spec = spec.and(UsuarioSpecification.comRole(filtro.getRole().name()));
        }
        if (filtro.getAtivo() != null) {
            spec = spec.and(UsuarioSpecification.comAtivo(filtro.getAtivo()));
        }

        return usuarioRepository.findAll(spec, pageable)
            .map(usuarioMapper::toRespostaDTO);
    }
} 