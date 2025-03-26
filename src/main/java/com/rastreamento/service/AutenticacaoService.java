package com.rastreamento.service;

import com.rastreamento.dto.LoginDTO;
import com.rastreamento.dto.TokenDTO;
import com.rastreamento.dto.UsuarioDTO;
import com.rastreamento.dto.UsuarioRespostaDTO;
import com.rastreamento.exception.CredenciaisInvalidasException;
import com.rastreamento.exception.EmailJaCadastradoException;
import com.rastreamento.mapper.UsuarioMapper;
import com.rastreamento.model.Usuario;
import com.rastreamento.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioMapper usuarioMapper;

    public AutenticacaoService(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder,
                             JwtService jwtService,
                             AuthenticationManager authenticationManager,
                             UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.usuarioMapper = usuarioMapper;
    }

    public TokenDTO autenticar(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha())
            );

            Usuario usuario = (Usuario) authentication.getPrincipal();
            usuario.setUltimoAcesso(LocalDateTime.now());
            usuarioRepository.save(usuario);

            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(token, "Bearer", jwtService.getExpirationTime(), usuario.getNome(), usuario.getRole());
        } catch (AuthenticationException e) {
            throw new CredenciaisInvalidasException("Credenciais inválidas");
        }
    }

    @Transactional
    public UsuarioRespostaDTO criarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuario.setRole(usuarioDTO.getRole());
        
        return usuarioMapper.toRespostaDTO(usuarioRepository.save(usuario));
    }

    public TokenDTO login(LoginDTO loginDTO) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha())
            );

            Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new CredenciaisInvalidasException("Usuário não encontrado"));

            String token = jwtService.gerarToken(usuario);

            return new TokenDTO(token, "Bearer", jwtService.getExpirationTime(), usuario.getNome(), usuario.getRole());
        } catch (Exception e) {
            throw new CredenciaisInvalidasException("Credenciais inválidas");
        }
    }
} 