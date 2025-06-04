package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe abstrata que representa uma pessoa no sistema.
 * Serve como base para as classes Cliente e Funcionario.
 * Contém os atributos e métodos comuns a todas as pessoas no sistema.
 */
public abstract class Pessoa {
    /** Nome completo da pessoa */
    @JsonProperty("nome")
    private String nome;
    
    /** Número de telefone para contato */
    @JsonProperty("telefone")
    private String telefone;
    
    /** Endereço completo da pessoa */
    @JsonProperty("endereco")
    private String endereco;

    /**
     * Construtor da classe Pessoa.
     * @param nome Nome completo da pessoa
     * @param telefone Número de telefone para contato
     * @param endereco Endereço completo da pessoa
     */
    public Pessoa(String nome, String telefone, String endereco) {
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    /**
     * Construtor vazio para o Jackson.
     * Necessário para a serialização/desserialização JSON.
     */
    protected Pessoa() {
    }

    /**
     * Retorna o nome da pessoa.
     * @return Nome completo da pessoa
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da pessoa.
     * @param nome Novo nome a ser definido
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o telefone da pessoa.
     * @return Número de telefone para contato
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * Define o telefone da pessoa.
     * @param telefone Novo número de telefone
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Retorna o endereço da pessoa.
     * @return Endereço completo
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Define o endereço da pessoa.
     * @param endereco Novo endereço
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * Retorna uma representação em string dos dados da pessoa.
     * @return String contendo nome, telefone e endereço da pessoa
     */
    @Override
    public String toString() {
        return "Pessoa{" +
                "nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", endereco='" + endereco + '\'' +
                '}';
    }
}