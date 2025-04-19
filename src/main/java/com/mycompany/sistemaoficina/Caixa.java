/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaoficina;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Caixa {
    private double saldo;
    private List<Transacao> transacoes;

    public Caixa() {
        this.saldo = 0;
        this.transacoes = new ArrayList<>();
    }

    private static class Transacao {
        private String tipo; // "Entrada" ou "Saída"
        private double valor;
        private String descricao;
        private String data;

        public Transacao(@JsonProperty("tipo")String tipo,@JsonProperty("valor") double valor,@JsonProperty("descricao") String descricao, @JsonProperty("data")String data) {
            this.tipo = tipo;
            this.valor = valor;
            this.descricao = descricao;
            this.data = data;
        }

        @Override
        public String toString() {
            return tipo + ": R$" + valor + " (" + descricao + ") - " + data;
        }
    }

    public void registrarEntrada(double valor, String descricao, String data) {
        saldo += valor;
        transacoes.add(new Transacao("Entrada", valor, descricao, data));
        System.out.println("Entrada registrada: R$" + valor + " - " + descricao);
    }

    public void registrarSaida(double valor, String descricao, String data) {
        if (valor <= saldo) {
            saldo -= valor;
            transacoes.add(new Transacao("Saída", valor, descricao, data));
            System.out.println("Saída registrada: R$" + valor + " - " + descricao);
        } else {
            System.out.println("Saldo insuficiente para esta saída!");
        }
    }

    public void gerarRelatorioDiario(String data) {
        System.out.println("\n=== RELATÓRIO DIÁRIO - " + data + " ===");
        System.out.println("Saldo inicial: R$" + (saldo - getTotalEntradas(data) + getTotalSaidas(data)));
        
        double totalEntradas = 0;
        double totalSaidas = 0;
        
        for (Transacao t : transacoes) {
            if (t.data.equals(data)) {
                System.out.println(t);
                if (t.tipo.equals("Entrada")) {
                    totalEntradas += t.valor;
                } else {
                    totalSaidas += t.valor;
                }
            }
        }
        
        System.out.println("\nTotal de entradas: R$" + totalEntradas);
        System.out.println("Total de saídas: R$" + totalSaidas);
        System.out.println("Saldo final: R$" + saldo);
    }

    private double getTotalEntradas(String data) {
        return transacoes.stream()
                .filter(t -> t.tipo.equals("Entrada") && t.data.equals(data))
                .mapToDouble(t -> t.valor)
                .sum();
    }

    private double getTotalSaidas(String data) {
        return transacoes.stream()
                .filter(t -> t.tipo.equals("Saída") && t.data.equals(data))
                .mapToDouble(t -> t.valor)
                .sum();
    }

    public double getSaldo() {
        return saldo;
    }

    public void gerarRelatorioMensal(int mes, int ano) {
        System.out.println("\n=== RELATÓRIO MENSAL - " + mes + "/" + ano + " ===");
        
        double totalEntradas = 0;
        double totalSaidas = 0;
        
        for (Transacao t : transacoes) {
            String[] dataParts = t.data.split("/");
            int tMes = Integer.parseInt(dataParts[1]);
            int tAno = Integer.parseInt(dataParts[2]);
            
            if (tMes == mes && tAno == ano) {
                System.out.println(t);
                if (t.tipo.equals("Entrada")) {
                    totalEntradas += t.valor;
                } else {
                    totalSaidas += t.valor;
                }
            }
        }
        
        System.out.println("\nTotal de entradas: R$" + totalEntradas);
        System.out.println("Total de saídas: R$" + totalSaidas);
        System.out.println("Saldo do mês: R$" + (totalEntradas - totalSaidas));
    }
}