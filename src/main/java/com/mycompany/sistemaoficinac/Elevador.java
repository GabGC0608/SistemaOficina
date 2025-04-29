package com.mycompany.sistemaoficinac;

/**
 * Classe que representa um elevador utilizado na oficina.
 */
public class Elevador {  

    private float peso; // Peso suportado pelo elevador
    private String modelo; // Modelo do elevador

    /**
     * Construtor da classe Elevador.
     *
     * @param peso Peso suportado pelo elevador.
     * @param modelo Modelo do elevador.
     */
    public Elevador(float peso, String modelo) {
        this.peso = peso;
        this.modelo = modelo;
    }

    /**
     * Obtém o peso suportado pelo elevador.
     *
     * @return Peso suportado.
     */
    public float getPeso() {
        return peso;
    }

    /**
     * Define o peso suportado pelo elevador.
     *
     * @param peso Novo peso suportado.
     */
    public void setPeso(float peso) {
        this.peso = peso;
    }

    /**
     * Obtém o modelo do elevador.
     *
     * @return Modelo do elevador.
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Define o modelo do elevador.
     *
     * @param modelo Novo modelo do elevador.
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}