package com.rastreamento.tcp;

import com.rastreamento.model.DadosLocalizacao;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServidorTCP {

    @Value("${tcp.server.port:8090}")
    private int porta;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String chaveRoteamento;

    @Value("${tcp.server.thread-pool-size:100}")
    private int tamanhoPoolThreads;

    @Value("${tcp.server.batch-size:100}")
    private int tamanhoLote;

    @Value("${tcp.server.batch-interval:1000}")
    private long intervaloLote;

    // Região do Brasil (aproximadamente)
    private static final double LAT_MIN = -33.0; // Extremo sul do Brasil
    private static final double LAT_MAX = 5.0;   // Extremo norte do Brasil
    private static final double LON_MIN = -74.0; // Extremo oeste do Brasil
    private static final double LON_MAX = -34.0; // Extremo leste do Brasil

    private ServerSocket servidorSocket;
    private final ExecutorService executorService = Executors.newFixedThreadPool(100);
    private final RabbitTemplate rabbitTemplate;
    private volatile boolean executando = true;
    private final BlockingQueue<DadosLocalizacao> filaMensagens = new LinkedBlockingQueue<>();
    private ScheduledExecutorService agendador;

    @PostConstruct
    public void iniciar() {
        // Inicia o agendador para processar mensagens em lote
        agendador = Executors.newScheduledThreadPool(1);
        agendador.scheduleAtFixedRate(this::processarLote, 0, intervaloLote, TimeUnit.MILLISECONDS);

        // Inicia o servidor TCP
        executorService.submit(() -> {
            try {
                servidorSocket = new ServerSocket(porta);
                System.out.println("Servidor TCP iniciado na porta " + porta);

                while (executando) {
                    Socket socketCliente = servidorSocket.accept();
                    socketCliente.setSoTimeout(30000); // 30 segundos
                    processarCliente(socketCliente);
                }
            } catch (Exception e) {
                if (executando) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validarCoordenadas(double lat, double lon) {
        if (lat < LAT_MIN || lat > LAT_MAX || lon < LON_MIN || lon > LON_MAX) {
            System.out.println("Coordenadas fora dos limites - Lat: " + lat + ", Lon: " + lon);
            System.out.println("Limites - Lat: [" + LAT_MIN + ", " + LAT_MAX + "], Lon: [" + LON_MIN + ", " + LON_MAX + "]");
            return false;
        }
        return true;
    }

    private void processarCliente(Socket socketCliente) {
        executorService.submit(() -> {
            try (BufferedReader leitor = new BufferedReader(
                    new InputStreamReader(socketCliente.getInputStream()))) {
                
                String linha;
                while ((linha = leitor.readLine()) != null) {
                    try {
                        String[] dados = linha.split(",");
                        if (dados.length >= 4) {
                            String idVeiculo = dados[0];
                            double latitude = Double.parseDouble(dados[1]);
                            double longitude = Double.parseDouble(dados[2]);
                            double velocidade = Double.parseDouble(dados[3]);

                            System.out.println("Recebido - Id: " + idVeiculo + ", Lat: " + latitude + ", Lon: " + longitude + ", Vel: " + velocidade);

                            // Valida as coordenadas
                            if (!validarCoordenadas(latitude, longitude)) {
                                continue;
                            }

                            DadosLocalizacao localizacao = new DadosLocalizacao();
                            localizacao.setIdVeiculo(idVeiculo);
                            localizacao.setLatitude(latitude);
                            localizacao.setLongitude(longitude);
                            localizacao.setVelocidade(velocidade);
                            localizacao.setDataHora(LocalDateTime.now());
                            localizacao.setAtivo(true);

                            System.out.println("Processando localização: " + localizacao);
                            
                            // Adiciona à fila de mensagens
                            filaMensagens.offer(localizacao);
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao processar mensagem: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                if (!socketCliente.isClosed()) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    socketCliente.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void processarLote() {
        List<DadosLocalizacao> lote = new ArrayList<>();
        filaMensagens.drainTo(lote, tamanhoLote);

        if (!lote.isEmpty()) {
            // Envia o lote para o RabbitMQ
            lote.forEach(localizacao -> {
                System.out.println("Enviando para RabbitMQ: " + localizacao);
                rabbitTemplate.convertAndSend(exchange, chaveRoteamento, localizacao);
            });
        }
    }

    @PreDestroy
    public void parar() {
        executando = false;
        try {
            if (servidorSocket != null && !servidorSocket.isClosed()) {
                servidorSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (agendador != null) {
            agendador.shutdown();
            try {
                if (!agendador.awaitTermination(60, TimeUnit.SECONDS)) {
                    agendador.shutdownNow();
                }
            } catch (InterruptedException e) {
                agendador.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
} 