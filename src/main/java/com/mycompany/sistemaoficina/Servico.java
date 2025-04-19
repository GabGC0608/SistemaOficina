package com.mycompany.sistemaoficina;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Servico {
    private String nome;
    private String descricao;
    private double valor;
    private int tempoEstimado; // em minutos

    public Servico(@JsonProperty("nome")String nome, @JsonProperty("descricao")String descricao,@JsonProperty("valor") double valor,@JsonProperty("tempoEstimadp") int tempoEstimado) {
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.tempoEstimado = tempoEstimado;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(int tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    @Override
    public String toString() {
        return nome + " - " + descricao + " (R$" + valor + ", " + tempoEstimado + " minutos)";
    }
}