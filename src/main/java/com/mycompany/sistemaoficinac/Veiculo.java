package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa um veículo.
 */
public class Veiculo {
    private String modelo; // Modelo do veículo
    private String placa;  // Placa do veículo
    private int ano;       // Ano de fabricação do veículo
    private String marca;  // Marca do veículo
    private String cor;    // Cor do veículo

    /**
     * Construtor da classe Veiculo.
     *
     * @param modelo Modelo do veículo.
     * @param placa Placa do veículo.
     * @param ano Ano de fabricação do veículo.
     * @param marca Marca do veículo.
     * @param cor Cor do veículo.
     */
    @JsonCreator
    public Veiculo(@JsonProperty("modelo") String modelo,
                   @JsonProperty("placa") String placa,
                   @JsonProperty("ano") int ano,
                   @JsonProperty("marca") String marca,
                   @JsonProperty("cor") String cor) {
        this.modelo = modelo;
        this.placa = placa;
        this.ano = ano;
        this.marca = marca;
        this.cor = cor;
    }

    /**
     * Obtém a placa do veículo.
     *
     * @return Placa do veículo.
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * Define a placa do veículo.
     *
     * @param placa Nova placa do veículo.
     */
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /**
     * Obtém o modelo do veículo.
     *
     * @return Modelo do veículo.
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Define o modelo do veículo.
     *
     * @param modelo Novo modelo do veículo.
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Obtém o ano de fabricação do veículo.
     *
     * @return Ano de fabricação do veículo.
     */
    public int getAno() {
        return ano;
    }

    /**
     * Define o ano de fabricação do veículo.
     *
     * @param ano Novo ano de fabricação do veículo.
     */
    public void setAno(int ano) {
        this.ano = ano;
    }

    /**
     * Obtém a marca do veículo.
     *
     * @return Marca do veículo.
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Define a marca do veículo.
     *
     * @param marca Nova marca do veículo.
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Obtém a cor do veículo.
     *
     * @return Cor do veículo.
     */
    public String getCor() {
        return cor;
    }

    /**
     * Define a cor do veículo.
     *
     * @param cor Nova cor do veículo.
     */
    public void setCor(String cor) {
        this.cor = cor;
    }

    /**
     * Retorna uma representação textual do veículo.
     *
     * @return String representando o veículo.
     */
    @Override
    public String toString() {
        return "Veículo: " + marca + " " + modelo + ", Placa: " + placa +
               ", Ano: " + ano + ", Cor: " + cor;
    }
}