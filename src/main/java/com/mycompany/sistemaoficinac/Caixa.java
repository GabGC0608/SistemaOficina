package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que gerencia o caixa da oficina, incluindo entradas, saídas e saldo.
 */
public class Caixa {
    private double saldo; // Saldo atual do caixa
    private List<Transacao> transacoes; // Lista de transações realizadas

    /**
     * Construtor da classe Caixa.
     * Inicializa o saldo como 0 e a lista de transações como vazia.
     */
    public Caixa() {
        this.saldo = 0;
        this.transacoes = new ArrayList<>();
    }

    /**
     * Classe interna que representa uma transação no caixa.
     */
    private static class Transacao {
        private String tipo; // Tipo da transação: "Entrada" ou "Saída"
        private double valor; // Valor da transação
        private String descricao; // Descrição da transação
        private String data; // Data da transação

        /**
         * Construtor da classe Transacao.
         *
         * @param tipo Tipo da transação.
         * @param valor Valor da transação.
         * @param descricao Descrição da transação.
         * @param data Data da transação.
         */
        public Transacao(String tipo, double valor, String descricao, String data) {
            this.tipo = tipo;
            this.valor = valor;
            this.descricao = descricao;
            this.data = data;
        }

        /**
         * Retorna uma representação textual da transação.
         *
         * @return String representando a transação.
         */
        @Override
        public String toString() {
            return tipo + ": R$" + valor + " - " + descricao + " (" + data + ")";
        }
    }  
    /**
     * Registra uma entrada no caixa.
     *
     * @param valor Valor da entrada.
     * @param descricao Descrição da entrada.
     * @param data Data da entrada.
     */
    public void registrarEntrada(double valor, String descricao, String data) {
        transacoes.add(new Transacao("Entrada", valor, descricao, data));
        saldo += valor;
        System.out.println("Entrada registrada: R$" + valor);
    }

    /**
     * Registra uma saída no caixa.
     *
     * @param valor Valor da saída.
     * @param descricao Descrição da saída.
     * @param data Data da saída.
     */
    public void registrarSaida(double valor, String descricao, String data) {
        transacoes.add(new Transacao("Saída", valor, descricao, data));
        saldo -= valor;
        System.out.println("Saída registrada: R$" + valor);
    }

    /**
     * Gera um relatório diário das transações realizadas em uma data específica.
     *
     * @param data Data para o relatório.
     */
    public void gerarRelatorioDiario(String data) {
        System.out.println("\n=== RELATÓRIO DIÁRIO (" + data + ") ===");
        for (Transacao transacao : transacoes) {
            if (transacao.data.equals(data)) {
                System.out.println(transacao);
            }
        }
        System.out.println("Total de entradas: R$" + getTotalEntradas(data));
        System.out.println("Total de saídas: R$" + getTotalSaidas(data));
    }

    /**
     * Calcula o total de entradas em uma data específica.
     *
     * @param data Data para o cálculo.
     * @return Total de entradas.
     */
    private double getTotalEntradas(String data) {
        return transacoes.stream()
                .filter(t -> t.tipo.equals("Entrada") && t.data.equals(data))
                .mapToDouble(t -> t.valor)
                .sum();
    }

    /**
     * Calcula o total de saídas em uma data específica.
     *
     * @param data Data para o cálculo.
     * @return Total de saídas.
     */
    private double getTotalSaidas(String data) {
        return transacoes.stream()
                .filter(t -> t.tipo.equals("Saída") && t.data.equals(data))
                .mapToDouble(t -> t.valor)
                .sum();
    }

    /**
     * Obtém o saldo atual do caixa.
     * 
     *
     * @return Saldo atual.
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     * Gera um relatório mensal das transações realizadas em um mês e ano específicos.
     *
     * @param mes Mês para o relatório.
     * @param ano Ano para o relatório.
     */
    public void gerarRelatorioMensal(int mes, int ano) {
        System.out.println("\n=== RELATÓRIO MENSAL (" + mes + "/" + ano + ") ===");
        for (Transacao transacao : transacoes) {
            String[] dataParts = transacao.data.split("/");
            int transacaoMes = Integer.parseInt(dataParts[1]);
            int transacaoAno = Integer.parseInt(dataParts[2]);
            if (transacaoMes == mes && transacaoAno == ano) {
                System.out.println(transacao);
            }
        }
    }
}