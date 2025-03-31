package com.rastreamento.service;

import com.rastreamento.dto.UsuarioDTO;
import com.rastreamento.dto.UsuarioFiltroDTO;
import com.rastreamento.dto.UsuarioRespostaDTO;
import com.rastreamento.dto.VinculoVeiculoDTO;
import com.rastreamento.exception.EmailJaCadastradoException;
import com.rastreamento.exception.NegocioException;
import com.rastreamento.converter.UsuarioConverter;
import com.rastreamento.converter.VeiculoConverter;
import com.rastreamento.converter.RastreadorConverter;
import com.rastreamento.model.Rastreador;
import com.rastreamento.model.Usuario;
import com.rastreamento.model.Veiculo;
import com.rastreamento.repository.RastreadorRepository;
import com.rastreamento.repository.UsuarioRepository;
import com.rastreamento.repository.VeiculoRepository;
import com.rastreamento.repository.UsuarioSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final VeiculoRepository veiculoRepository;
    private final RastreadorRepository rastreadorRepository;
    private final UsuarioConverter usuarioConverter;
    private final VeiculoConverter veiculoConverter;
    private final RastreadorConverter rastreadorConverter;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioRespostaDTO criar(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        Usuario usuario = usuarioConverter.toEntity(usuarioDTO);
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuario.setAtivo(true);

        usuario = usuarioRepository.save(usuario);
        return usuarioConverter.toRespostaDTO(usuario);
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
            .map(usuarioConverter::toRespostaDTO);
    }

    @Transactional
    public void vincularVeiculo(VinculoVeiculoDTO vinculoDTO) {
        Usuario usuario = usuarioRepository.findById(vinculoDTO.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // Validar se já existe veículo com a mesma placa ou chassi
        if (veiculoRepository.existsByPlaca(vinculoDTO.getPlaca())) {
            throw new NegocioException("Já existe um veículo cadastrado com esta placa");
        }
        if (veiculoRepository.existsByChassi(vinculoDTO.getChassi())) {
            throw new NegocioException("Já existe um veículo cadastrado com este chassi");
        }

        // Validar se já existe rastreador com o mesmo IMEI ou número de série
        if (rastreadorRepository.existsByImei(vinculoDTO.getImei())) {
            throw new NegocioException("Já existe um rastreador cadastrado com este IMEI");
        }
        if (rastreadorRepository.existsByNumero(vinculoDTO.getNumeroSerie())) {
            throw new NegocioException("Já existe um rastreador cadastrado com este número de série");
        }

        // Criar e configurar o rastreador
        Rastreador rastreador = new Rastreador();
        rastreador.setImei(vinculoDTO.getImei());
        rastreador.setNumero(vinculoDTO.getNumeroSerie());
        rastreador.setOperadora(vinculoDTO.getOperadora());
        
        // Salvar o rastreador primeiro
        rastreador = rastreadorRepository.save(rastreador);

        // Criar e configurar o veículo
        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(vinculoDTO.getPlaca());
        veiculo.setModelo(vinculoDTO.getModelo());
        veiculo.setMarca(vinculoDTO.getMarca());
        veiculo.setCor(vinculoDTO.getCor());
        veiculo.setAno(vinculoDTO.getAno());
        veiculo.setChassi(vinculoDTO.getChassi());
        veiculo.setUsuario(usuario);
        veiculo.setRastreador(rastreador);

        // Salvar o veículo
        veiculoRepository.save(veiculo);
    }
    
    @Transactional
    public void excluirUsuario(Long id) {
    	usuarioRepository.deleteById(id);
    }
} 