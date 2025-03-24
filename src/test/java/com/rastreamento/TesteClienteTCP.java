package com.rastreamento;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TesteClienteTCP {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5001);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {
            
            System.out.println("Conectado ao servidor!");
            System.out.println("Digite uma mensagem (ou 'sair' para encerrar):");
            
            String mensagem;
            while (!(mensagem = scanner.nextLine()).equalsIgnoreCase("sair")) {
                out.println(mensagem);
                System.out.println("Mensagem enviada: " + mensagem);
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 