package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Classe que representa um registro de ponto de um funcionário.
 * Armazena informações sobre entrada e saída do funcionário.
 */
public class PontoFuncionario {
    /** Matrícula do funcionário */
    private String matricula;
    /** Data e hora de entrada do funcionário */
    private LocalDateTime entrada;
    /** Data e hora de saída do funcionário */
    private LocalDateTime saida;

    /**
     * Construtor para criar um novo registro de ponto.
     * @param matricula Matrícula do funcionário
     * @param entrada Data e hora de entrada
     * @param saida Data e hora de saída (pode ser null se ainda não registrada)
     */
    @JsonCreator
    public PontoFuncionario(@JsonProperty("matricula") String matricula,
                           @JsonProperty("entrada") LocalDateTime entrada,
                           @JsonProperty("saida") LocalDateTime saida) {
        this.matricula = matricula;
        this.entrada = entrada;
        this.saida = saida;
    }

    /**
     * Retorna a matrícula do funcionário.
     * @return A matrícula do funcionário
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Retorna a data e hora de entrada do funcionário.
     * @return Data e hora de entrada
     */
    public LocalDateTime getEntrada() {
        return entrada;
    }

    /**
     * Retorna a data e hora de saída do funcionário.
     * @return Data e hora de saída, ou null se ainda não registrada
     */
    public LocalDateTime getSaida() {
        return saida;
    }

    /**
     * Define a data e hora de saída do funcionário.
     * @param saida Nova data e hora de saída
     */
    public void setSaida(LocalDateTime saida) {
        this.saida = saida;
    }

    /**
     * Retorna uma representação em string do registro de ponto.
     * @return String contendo matrícula, entrada e saída do funcionário
     */
    @Override
    public String toString() {
        return "PontoFuncionario{" +
               "matricula='" + matricula + '\'' +
               ", entrada=" + entrada +
               ", saida=" + saida +
               '}';
    }
}