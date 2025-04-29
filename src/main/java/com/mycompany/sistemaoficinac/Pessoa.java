package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa uma pessoa.
 * Contém informações básicas como nome, telefone e endereço.
 */
public class Pessoa {
    private String nome; // Nome da pessoa
    private String telefone; // Telefone da pessoa
    private String endereco; // Endereço da pessoa

    /**
     * Construtor da classe Pessoa.
     *
     * @param nome Nome da pessoa.
     * @param telefone Telefone da pessoa.
     * @param endereco Endereço da pessoa.
     */
    @JsonCreator
    public Pessoa(
            @JsonProperty("nome") String nome,
            @JsonProperty("telefone") String telefone,
            @JsonProperty("endereco") String endereco) {
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    /**
     * Obtém o nome da pessoa.
     *
     * @return Nome da pessoa.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da pessoa.
     *
     * @param nome Novo nome da pessoa.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém o telefone da pessoa.
     *
     * @return Telefone da pessoa.
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * Define o telefone da pessoa.
     *
     * @param telefone Novo telefone da pessoa.
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Obtém o endereço da pessoa.
     *
     * @return Endereço da pessoa.
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Define o endereço da pessoa.
     *
     * @param endereco Novo endereço da pessoa.
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * Retorna uma representação textual da pessoa.
     *
     * @return String representando a pessoa.
     */
    @Override
    public String toString() {
        return "Nome: " + nome + ", Telefone: " + telefone + ", Endereço: " + endereco;
    }
}