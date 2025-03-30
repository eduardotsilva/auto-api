package com.rastreamento.service;

import com.rastreamento.dto.LoginDTO;
import com.rastreamento.dto.TokenDTO;
import com.rastreamento.dto.UsuarioDTO;
import com.rastreamento.dto.UsuarioRespostaDTO;
import com.rastreamento.exception.CredenciaisInvalidasException;
import com.rastreamento.exception.EmailJaCadastradoException;
import com.rastreamento.converter.UsuarioConverter;
import com.rastreamento.model.Usuario;
import com.rastreamento.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioConverter usuarioConverter;

    public AutenticacaoService(UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder,
                             JwtService jwtService,
                             AuthenticationManager authenticationManager,
                             UsuarioConverter usuarioConverter) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.usuarioConverter = usuarioConverter;
    }

    public TokenDTO autenticar(LoginDTO loginDTO) {
        log.info("Tentativa de autenticação para o email: {}", loginDTO.getEmail());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha())
            );

            Usuario usuario = (Usuario) authentication.getPrincipal();
            usuario.setUltimoAcesso(LocalDateTime.now());
            usuarioRepository.save(usuario);

            String token = jwtService.gerarToken(usuario);
            log.info("Autenticação bem-sucedida para o email: {}", loginDTO.getEmail());
            return new TokenDTO(token, "Bearer", jwtService.getExpirationTime(), usuario.getNome(), usuario.getRole());
        } catch (BadCredentialsException e) {
            log.error("Credenciais inválidas para o email: {}", loginDTO.getEmail());
            throw new CredenciaisInvalidasException("Email ou senha incorretos");
        } catch (AuthenticationException e) {
            log.error("Erro de autenticação para o email: {}: {}", loginDTO.getEmail(), e.getMessage());
            throw new CredenciaisInvalidasException("Erro durante a autenticação");
        } catch (Exception e) {
            log.error("Erro inesperado durante a autenticação para o email: {}", loginDTO.getEmail(), e);
            throw new RuntimeException("Erro interno durante a autenticação");
        }
    }

    @Transactional
    public UsuarioRespostaDTO criarUsuario(UsuarioDTO usuarioDTO) {
        log.info("Tentativa de criação de usuário com email: {}", usuarioDTO.getEmail());
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            log.warn("Tentativa de criar usuário com email já cadastrado: {}", usuarioDTO.getEmail());
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        try {
            Usuario usuario = usuarioConverter.toEntity(usuarioDTO);
            usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
            usuario.setRole(usuarioDTO.getRole());
            usuario.setDataCriacao(LocalDateTime.now());
            usuario.setAtivo(true);
            
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            log.info("Usuário criado com sucesso: {}", usuarioDTO.getEmail());
            return usuarioConverter.toRespostaDTO(usuarioSalvo);
        } catch (Exception e) {
            log.error("Erro ao criar usuário com email: {}", usuarioDTO.getEmail(), e);
            throw new RuntimeException("Erro ao criar usuário");
        }
    }

    public TokenDTO login(LoginDTO loginDTO) {
        log.info("Tentativa de login para o email: {}", loginDTO.getEmail());
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha())
            );

            Usuario usuario = usuarioRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado para o email: {}", loginDTO.getEmail());
                    return new CredenciaisInvalidasException("Usuário não encontrado");
                });

            usuario.setUltimoAcesso(LocalDateTime.now());
            usuarioRepository.save(usuario);

            String token = jwtService.gerarToken(usuario);
            log.info("Login bem-sucedido para o email: {}", loginDTO.getEmail());
            return new TokenDTO(token, "Bearer", jwtService.getExpirationTime(), usuario.getNome(), usuario.getRole());
        } catch (BadCredentialsException e) {
            log.error("Credenciais inválidas para o email: {}", loginDTO.getEmail());
            throw new CredenciaisInvalidasException("Email ou senha incorretos");
        } catch (CredenciaisInvalidasException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado durante o login para o email: {}", loginDTO.getEmail(), e);
            throw new RuntimeException("Erro interno durante o login");
        }
    }
} 