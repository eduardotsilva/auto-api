package com.rastreamento.infrastructure.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.rastreamento.domain.service.RastreamentoService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RastreadorTcpServer {

    private final RastreamentoService rastreamentoService;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private static final int PORT = 5432;
    private volatile boolean running = true;

    @PostConstruct
    public void start() {
        executorService = Executors.newCachedThreadPool();
        executorService.submit(this::runServer);
    }

    @PreDestroy
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (Exception e) {
            log.error("Erro ao fechar o servidor TCP", e);
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private void runServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            log.info("Servidor TCP iniciado na porta {}", PORT);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                log.info("Nova conexão recebida de: {}", clientSocket.getInetAddress());
                executorService.submit(() -> handleClient(clientSocket));
            }
        } catch (Exception e) {
            if (running) {
                log.error("Erro no servidor TCP", e);
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String line;
            while (running && (line = reader.readLine()) != null) {
                log.info("Dados recebidos: {}", line);
                rastreamentoService.processarDadosRastreador(line);
            }
        } catch (Exception e) {
            log.error("Erro ao processar dados do cliente: {}", clientSocket.getInetAddress(), e);
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                log.error("Erro ao fechar conexão do cliente", e);
            }
        }
    }
} 