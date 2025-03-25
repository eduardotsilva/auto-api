package com.rastreamento;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Locale;

public class TesteClienteTCP {

    private static final String HOST = "localhost";
    private static final int PORTA = 8090;
    private static final int INTERVALO_ENVIO_MS = 2000;

    // Região do Brasil (aproximadamente)
    private static final double LAT_MIN = -33.0; // Extremo sul do Brasil
    private static final double LAT_MAX = 5.0;   // Extremo norte do Brasil
    private static final double LON_MIN = -74.0; // Extremo oeste do Brasil
    private static final double LON_MAX = -34.0; // Extremo leste do Brasil

    private static class Veiculo {
        String id;
        double lat;
        double lon;
        double velocidade;
        double direcao; // em radianos

        public Veiculo(String id) {
            this.id = id;
            Random r = new Random();
            // Inicializa em uma posição aleatória dentro dos limites
            this.lat = LAT_MIN + r.nextDouble() * (LAT_MAX - LAT_MIN);
            this.lon = LON_MIN + r.nextDouble() * (LON_MAX - LON_MIN);
            this.velocidade = 30 + r.nextDouble() * 40; // entre 30 e 70 km/h
            this.direcao = r.nextDouble() * 2 * Math.PI;
        }

        public void mover() {
            Random r = new Random();
            // Altera levemente a direção (-15 a +15 graus)
            this.direcao += (r.nextDouble() - 0.5) * Math.PI / 6;
            
            // Calcula o deslocamento baseado na velocidade (convertida para graus/segundo)
            double deslocamento = (this.velocidade / 3600) * (INTERVALO_ENVIO_MS / 1000.0) / 111.32;
            
            // Atualiza posição
            this.lat += deslocamento * Math.cos(this.direcao);
            this.lon += deslocamento * Math.sin(this.direcao);

            // Mantém dentro dos limites
            if (this.lat < LAT_MIN || this.lat > LAT_MAX || this.lon < LON_MIN || this.lon > LON_MAX) {
                this.direcao += Math.PI; // Inverte a direção
                // Força dentro dos limites
                this.lat = Math.max(LAT_MIN, Math.min(LAT_MAX, this.lat));
                this.lon = Math.max(LON_MIN, Math.min(LON_MAX, this.lon));
            }

            // Varia um pouco a velocidade
            this.velocidade += (r.nextDouble() - 0.5) * 5;
            this.velocidade = Math.max(20, Math.min(80, this.velocidade));
        }

        @Override
        public String toString() {
            return String.format("%s,%.6f,%.6f,%.1f", id, lat, lon, velocidade);
        }
    }

    public static void main(String[] args) {
        // Força o uso do ponto como separador decimal
        Locale.setDefault(Locale.US);

        // Cria 3 veículos
        Veiculo[] veiculos = new Veiculo[3];
        for (int i = 0; i < veiculos.length; i++) {
            veiculos[i] = new Veiculo("VEICULO" + (i + 1));
        }

        try (Socket socket = new Socket(HOST, PORTA);
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {

            System.out.println("Conectado ao servidor TCP na porta " + PORTA);

            while (true) {
                for (Veiculo veiculo : veiculos) {
                    veiculo.mover();
                    String mensagem = String.format(Locale.US, "%s,%.6f,%.6f,%.1f", 
                        veiculo.id, 
                        veiculo.lat,
                        veiculo.lon,
                        veiculo.velocidade);
                    writer.println(mensagem);
                    System.out.println("Enviado: " + mensagem);
                }

                Thread.sleep(INTERVALO_ENVIO_MS);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
