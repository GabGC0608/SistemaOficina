package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class PontoFuncionario {
    private String matricula;
    private LocalDateTime entrada;
    private LocalDateTime saida;

    @JsonCreator
    public PontoFuncionario(@JsonProperty("matricula") String matricula,
                           @JsonProperty("entrada") LocalDateTime entrada,
                           @JsonProperty("saida") LocalDateTime saida) {
        this.matricula = matricula;
        this.entrada = entrada;
        this.saida = saida;
    }

    // Getters e Setters
    public String getMatricula() {
        return matricula;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public void setSaida(LocalDateTime saida) {
        this.saida = saida;
    }

    @Override
    public String toString() {
        return "PontoFuncionario{" +
               "matricula='" + matricula + '\'' +
               ", entrada=" + entrada +
               ", saida=" + saida +
               '}';
    }
}