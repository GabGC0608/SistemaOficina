package com.mycompany.sistemaoficina;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Veiculo {
    private String modelo;
    private String placa;
    private int ano;
    private String marca;
    private String cor;

    public Veiculo(@JsonProperty("modelo")String modelo,@JsonProperty("placa") String placa,@JsonProperty("ano") int ano,@JsonProperty("marca") String marca,@JsonProperty("cor") String cor) {
        this.modelo = modelo;
        this.placa = placa;
        this.ano = ano;
        this.marca = marca;
        this.cor = cor;
    }
    
     public String getPlaca() {
        return placa;
    }
     
    // Getters e Setters
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }


    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    @Override
    public String toString() {
        return "Veículo: " + marca + " " + modelo + ", Placa: " + placa + 
               ", Ano: " + ano + ", Cor: " + cor;
    }
}