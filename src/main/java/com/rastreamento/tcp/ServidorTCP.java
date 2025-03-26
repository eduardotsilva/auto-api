package com.rastreamento.tcp;

import com.rastreamento.model.DadosLocalizacao;
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

    @Value("${tcp.server.queue-capacity:10000}")
    private int capacidadeFila;

    // Região do Brasil (aproximadamente)
    private static final double LAT_MIN = -33.0; // Extremo sul do Brasil
    private static final double LAT_MAX = 5.0;   // Extremo norte do Brasil
    private static final double LON_MIN = -74.0; // Extremo oeste do Brasil
    private static final double LON_MAX = -34.0; // Extremo leste do Brasil

    private ServerSocket servidorSocket;
    private ThreadPoolExecutor executorService;
    private final RabbitTemplate rabbitTemplate;
    private volatile boolean executando = true;
    private BlockingQueue<DadosLocalizacao> filaMensagens;
    private ScheduledExecutorService agendador;
    private final List<Socket> conexoesAtivas = new CopyOnWriteArrayList<>();

    public ServidorTCP(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void iniciar() {
        this.filaMensagens = new LinkedBlockingQueue<>(capacidadeFila);
        
        // Configuração do pool de threads com política de rejeição
        this.executorService = new ThreadPoolExecutor(
            tamanhoPoolThreads / 2,    // núcleo
            tamanhoPoolThreads,        // máximo
            60L,                       // tempo ocioso
            TimeUnit.SECONDS,          // unidade de tempo
            new ArrayBlockingQueue<>(tamanhoPoolThreads * 2), // fila de trabalhos
            new ThreadPoolExecutor.CallerRunsPolicy()         // política de rejeição
        );

        // Inicia o agendador para processar mensagens em lote
        agendador = Executors.newScheduledThreadPool(2); // 2 threads para processamento em lote
        
        // Agenda o processamento de lotes
        agendador.scheduleAtFixedRate(this::processarLote, 0, intervaloLote, TimeUnit.MILLISECONDS);
        
        // Agenda o monitoramento de métricas
        agendador.scheduleAtFixedRate(this::reportarMetricas, 0, 60, TimeUnit.SECONDS);

        // Inicia o servidor TCP
        executorService.submit(() -> {
            try {
                servidorSocket = new ServerSocket(porta);
                System.out.println("Servidor TCP iniciado na porta " + porta);

                while (executando) {
                    Socket socketCliente = servidorSocket.accept();
                    socketCliente.setSoTimeout(30000); // 30 segundos
                    conexoesAtivas.add(socketCliente);
                    processarCliente(socketCliente);
                }
            } catch (Exception e) {
                if (executando) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void reportarMetricas() {
        System.out.println("\n=== Métricas do Servidor TCP ===");
        System.out.println("Conexões ativas: " + conexoesAtivas.size());
        System.out.println("Tamanho da fila de mensagens: " + filaMensagens.size());
        System.out.println("Thread pool - Ativos: " + executorService.getActiveCount() + 
                         ", Pool size: " + executorService.getPoolSize() + 
                         ", Máximo: " + executorService.getLargestPoolSize());
        System.out.println("==============================\n");
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
                while ((linha = leitor.readLine()) != null && executando) {
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
                            localizacao.setImei(idVeiculo);
                            localizacao.setLatitude(latitude);
                            localizacao.setLongitude(longitude);
                            localizacao.setVelocidade(velocidade);
                            localizacao.setDataHora(LocalDateTime.now());
                            localizacao.setAtivo(true);
                            localizacao.setStatus("A"); // Status válido por padrão
                            localizacao.setDirecao("0"); // Direção padrão

                            // Tenta adicionar à fila com timeout
                            if (!filaMensagens.offer(localizacao, 5, TimeUnit.SECONDS)) {
                                System.err.println("Fila de mensagens cheia, descartando localização: " + localizacao);
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao processar mensagem: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                if (!socketCliente.isClosed() && executando) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    conexoesAtivas.remove(socketCliente);
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
            int tentativas = 0;
            boolean sucesso = false;
            
            while (!sucesso && tentativas < 3 && executando) {
                try {
                    // Envia o lote para o RabbitMQ
                    lote.forEach(localizacao -> {
                        rabbitTemplate.convertAndSend(exchange, chaveRoteamento, localizacao);
                    });
                    sucesso = true;
                } catch (Exception e) {
                    tentativas++;
                    System.err.println("Erro ao enviar lote para RabbitMQ (tentativa " + tentativas + "/3): " + e.getMessage());
                    try {
                        Thread.sleep(1000 * tentativas); // Backoff exponencial
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            if (!sucesso) {
                System.err.println("Falha ao enviar lote após 3 tentativas. Mensagens serão perdidas.");
            }
        }
    }

    @PreDestroy
    public void parar() {
        executando = false;
        
        // Fecha todas as conexões ativas
        for (Socket socket : conexoesAtivas) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        conexoesAtivas.clear();

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

        // Processa mensagens restantes antes de desligar
        processarLote();

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