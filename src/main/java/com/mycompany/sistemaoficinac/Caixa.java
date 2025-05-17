package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que gerencia o caixa da oficina, incluindo entradas, saídas e saldo.
 */
public class Caixa {
    private double saldo; // Saldo atual do caixa
    private List<Transacao> transacoes; // Lista de transações realizadas

    public Caixa() {
        this.saldo = 0;
        this.transacoes = new ArrayList<>();
    }

    public static class Transacao {
        private String tipo;
        private double valor;
        private String descricao;
        private String data;
        private String categoria;
        private String responsavel;
        private String cliente;

        @JsonCreator
        public Transacao(@JsonProperty("tipo") String tipo,
                        @JsonProperty("valor") double valor,
                        @JsonProperty("descricao") String descricao,
                        @JsonProperty("data") String data,
                        @JsonProperty("categoria") String categoria,
                        @JsonProperty("responsavel") String responsavel,
                        @JsonProperty("cliente") String cliente) {
            this.tipo = tipo;
            this.valor = valor;
            this.descricao = descricao;
            this.data = data;
            this.categoria = categoria;
            this.responsavel = responsavel;
            this.cliente = cliente;
        }

        // Getters
        public String getTipo() { return tipo; }
        public double getValor() { return valor; }
        public String getDescricao() { return descricao; }
        public String getData() { return data; }
        public String getCategoria() { return categoria; }
        public String getResponsavel() { return responsavel; }
        public String getCliente() { return cliente; }

        @Override
        public String toString() {
            return String.format("%s: R$ %.2f - %s (%s) [%s]", tipo, valor, descricao, data, categoria);
        }
    }

    // Métodos para manipular transações
    public List<Transacao> getTransacoes() {
        return new ArrayList<>(transacoes);
    }

    // Método único para registrar entrada
    public void registrarEntrada(double valor, String descricao, String data, String categoria, String responsavel, String cliente) {
        Transacao t = new Transacao("Entrada", valor, descricao, data, categoria, responsavel, cliente);
        transacoes.add(t);
        saldo += valor;
        System.out.println("Entrada registrada: " + t);
    }

    // Método único para registrar saída
    public void registrarSaida(double valor, String descricao, String data, String categoria, String responsavel, String cliente) {
        Transacao t = new Transacao("Saída", valor, descricao, data, categoria, responsavel, cliente);
        transacoes.add(t);
        saldo -= valor;
        System.out.println("Saída registrada: " + t);
    }

    // Métodos simplificados (chamam o método completo com valores padrão)
    public void registrarEntrada(double valor, String descricao, String data, String categoria) {
        registrarEntrada(valor, descricao, data, categoria, "Sistema", null);
    }

    public void registrarSaida(double valor, String descricao, String data, String categoria) {
        registrarSaida(valor, descricao, data, categoria, "Sistema", null);
    }

    // Métodos de filtro
    public List<Transacao> getTransacoesPorPeriodo(String dataInicio, String dataFim) {
        return transacoes.stream()
                .filter(t -> t.data.compareTo(dataInicio) >= 0 && t.data.compareTo(dataFim) <= 0)
                .collect(Collectors.toList());
    }

    public List<Transacao> getTransacoesPorCategoria(String categoria) {
        return transacoes.stream()
                .filter(t -> t.categoria.equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    // Métodos de relatório
    public void gerarRelatorioDiario(String data) {
        System.out.println("\n=== RELATÓRIO DIÁRIO (" + data + ") ===");
        transacoes.stream()
            .filter(t -> t.data.equals(data))
            .forEach(System.out::println);
        
        System.out.println("Total de entradas: R$" + getTotalEntradas(data));
        System.out.println("Total de saídas: R$" + getTotalSaidas(data));
        System.out.println("Saldo do dia: R$" + (getTotalEntradas(data) - getTotalSaidas(data)));
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
        System.out.println("\n=== RELATÓRIO MENSAL (" + mes + "/" + ano + ") ===");
        
        double totalEntradas = 0;
        double totalSalarios = 0;
        double totalPecas = 0;
        double outrasDespesas = 0;
        
        for (Transacao transacao : transacoes) {
            String[] dataParts = transacao.data.split("/");
            int transacaoMes = Integer.parseInt(dataParts[1]);
            int transacaoAno = Integer.parseInt(dataParts[2]);
            
            if (transacaoMes == mes && transacaoAno == ano) {
                System.out.println(transacao);
                
                if (transacao.tipo.equals("Entrada")) {
                    totalEntradas += transacao.valor;
                } else {
                    if (transacao.descricao.contains("Pagamento de salários")) {
                        totalSalarios += transacao.valor;
                    } else if (transacao.descricao.contains("Compra de peças")) {
                        totalPecas += transacao.valor;
                    } else {
                        outrasDespesas += transacao.valor;
                    }
                }
            }
        }
        
        System.out.println("\n=== RESUMO FINANCEIRO ===");
        System.out.printf("Total de entradas: R$ %.2f%n", totalEntradas);
        System.out.printf("Total de despesas: R$ %.2f%n", (totalSalarios + totalPecas + outrasDespesas));
        System.out.println("\nDetalhamento de despesas:");
        System.out.printf("- Salários: R$ %.2f%n", totalSalarios);
        System.out.printf("- Peças e materiais: R$ %.2f%n", totalPecas);
        System.out.printf("- Outras despesas: R$ %.2f%n", outrasDespesas);
        System.out.printf("\nSaldo final: R$ %.2f%n", (totalEntradas - (totalSalarios + totalPecas + outrasDespesas)));
    }
}