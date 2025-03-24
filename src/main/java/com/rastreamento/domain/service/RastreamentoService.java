package com.rastreamento.domain.service;

import com.rastreamento.domain.model.Localizacao;
import com.rastreamento.domain.repository.LocalizacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RastreamentoService {

    private final LocalizacaoRepository localizacaoRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("ddMMyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");

    @Transactional
    public void processarDadosRastreador(String dados) {
        log.info("Processando dados do rastreador: {}", dados);
        
        try {
            // Exemplo de formato esperado: $GPRMC,123456,A,1234.5678,S,1234.5678,W,0.0,0.0,010124,0.0,E,A*00
            String[] partes = dados.split(",");
            
            if (partes.length < 12) {
                log.error("Formato de dados inválido: {}", dados);
                return;
            }

            Localizacao localizacao = new Localizacao();
            localizacao.setImei(partes[0].substring(1)); // Remove o $ inicial
            localizacao.setStatus(partes[2]); // A = válido, V = inválido
            
            if ("A".equals(partes[2])) {
                // Converter coordenadas
                localizacao.setLatitude(converterCoordenada(partes[3], partes[4]));
                localizacao.setLongitude(converterCoordenada(partes[5], partes[6]));
                localizacao.setVelocidade(Double.parseDouble(partes[7]));
                localizacao.setDirecao(partes[8]);
                
                // Converter data e hora
                String data = partes[9];
                String hora = partes[1];
                LocalDateTime dataHora = LocalDateTime.parse(
                    data + hora,
                    DateTimeFormatter.ofPattern("ddMMyyHHmmss")
                );
                localizacao.setDataHora(dataHora);
            }

            localizacaoRepository.save(localizacao);
            log.info("Localização salva com sucesso: {}", localizacao);
        } catch (Exception e) {
            log.error("Erro ao processar dados do rastreador: {}", e.getMessage(), e);
        }
    }

    private Double converterCoordenada(String valor, String direcao) {
        if (valor == null || valor.isEmpty()) {
            return null;
        }

        double graus = Double.parseDouble(valor.substring(0, 2));
        double minutos = Double.parseDouble(valor.substring(2));
        double coordenada = graus + (minutos / 60.0);

        if ("S".equals(direcao) || "W".equals(direcao)) {
            coordenada = -coordenada;
        }

        return coordenada;
    }

    public List<Localizacao> buscarUltimasLocalizacoes(String imei) {
        return localizacaoRepository.findByImeiOrderByDataHoraDesc(imei);
    }
} 