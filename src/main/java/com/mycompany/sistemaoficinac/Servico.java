package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa um serviço oferecido pela oficina.
 */
public class Servico {
    private String nome; // Nome do serviço
    private String descricao; // Descrição do serviço
    private double valor; // Valor do serviço
    private int tempoEstimado; // Tempo estimado para execução do serviço (em minutos)
    private boolean requerElevador; // Indica se o serviço requer elevador

    /**
     * Construtor da classe Servico.
     *
     * @param nome Nome do serviço.
     * @param descricao Descrição do serviço.
     * @param valor Valor do serviço.
     * @param tempoEstimado Tempo estimado para execução do serviço (em minutos).
     */
    @JsonCreator
    public Servico(@JsonProperty("nome") String nome,
                   @JsonProperty("descricao") String descricao,
                   @JsonProperty("valor") double valor,
                   @JsonProperty("tempoEstimado") int tempoEstimado) {
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.tempoEstimado = tempoEstimado;
        this.requerElevador = false; // Por padrão, o serviço não requer elevador
    }

    /**
     * Obtém o nome do serviço.
     *
     * @return Nome do serviço.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do serviço.
     *
     * @param nome Novo nome do serviço.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a descrição do serviço.
     *
     * @return Descrição do serviço.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição do serviço.
     *
     * @param descricao Nova descrição do serviço.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtém o valor do serviço.
     *
     * @return Valor do serviço.
     */
    public double getValor() {
        return valor;
    }

    /**
     * Define o valor do serviço.
     *
     * @param valor Novo valor do serviço.
     */
    public void setValor(double valor) {
        this.valor = valor;
    }

    /**
     * Obtém o tempo estimado para execução do serviço.
     *
     * @return Tempo estimado em minutos.
     */
    public int getTempoEstimado() {
        return tempoEstimado;
    }

    /**
     * Define o tempo estimado para execução do serviço.
     *
     * @param tempoEstimado Novo tempo estimado em minutos.
     */
    public void setTempoEstimado(int tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    /**
     * Verifica se o serviço requer elevador.
     *
     * @return {@code true} se o serviço requer elevador, caso contrário {@code false}.
     */
    public boolean isRequerElevador() {
        return requerElevador;
    }

    /**
     * Define se o serviço requer elevador.
     *
     * @param requerElevador {@code true} se o serviço requer elevador, caso contrário {@code false}.
     */
    public void setRequerElevador(boolean requerElevador) {
        this.requerElevador = requerElevador;
    }

    /**
     * Retorna uma representação textual do serviço.
     *
     * @return String representando o serviço.
     */
    @Override
    public String toString() {
        return nome + " - " + descricao + " (R$" + valor + ", " + tempoEstimado + " minutos)" +
               (requerElevador ? " [Requer Elevador]" : "");
    }
}