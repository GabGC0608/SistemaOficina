package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe que representa um cliente da oficina.
 */
public class Cliente extends Pessoa {
    private List<Veiculo> veiculos; // Lista de veículos do cliente
    private static int contadorVeiculos = 0; // Contador de veículos (encapsulado)
    /**
     * Construtor da classe Cliente com lista de veículos.
     *
     * @param nome Nome do cliente.
     * @param telefone Telefone do cliente.
     * @param endereco Endereço do cliente.
     * @param veiculos Lista de veículos do cliente.
     */
    public Cliente(@JsonProperty("nome") String nome,
                   @JsonProperty("telefone") String telefone,
                   @JsonProperty("endereco") String endereco,
                   @JsonProperty(value = "veiculos", required = false) List<Veiculo> veiculos) {
        super(nome, telefone, endereco);
        this.veiculos = veiculos != null ? veiculos : new ArrayList<>();
    }

    /**
     * Construtor da classe Cliente sem lista de veículos.
     *
     * @param nome Nome do cliente.
     * @param telefone Telefone do cliente.
     * @param endereco Endereço do cliente.
     */
    public Cliente(String nome, String telefone, String endereco) {
        super(nome, telefone, endereco);
        this.veiculos = new ArrayList<>();
    }
    /***
     * Método que retorna o contador de veículos.
     * @return contadorVeiculos
     */
    public static int getContadorVeiculos() {
        return contadorVeiculos;
    }
    /***
     * Método que retorna o contador de veículos.
     */
    private static void incrementarContadorVeiculos() {
        contadorVeiculos++;
    }

    /**
     * Adiciona um veículo à lista de veículos do cliente.
     *
     * @param veiculo Veículo a ser adicionado.
     */
    public void adicionarVeiculo(Veiculo veiculo) {
        veiculos.add(veiculo);
        incrementarContadorVeiculos(); // Incrementa o contador de veículos
        System.out.println("Veículo adicionado com sucesso!");
    }

    /**
     * Remove um veículo da lista de veículos do cliente com base na placa.
     *
     * @param placa Placa do veículo a ser removido.
     */
    public void removerVeiculo(String placa) {
        veiculos.removeIf(veiculo -> veiculo.getPlaca().equalsIgnoreCase(placa));
        System.out.println("Veículo removido com sucesso!");
    }

    /**
     * Obtém a lista de veículos do cliente.
     *
     * @return Lista de veículos.
     */
    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    /**
     * Lista todos os veículos do cliente.
     */
    public void listarVeiculos() {
        System.out.println("\n=== VEÍCULOS DO CLIENTE ===");
        for (Veiculo veiculo : veiculos) {
            System.out.println(veiculo);
        }
    }
}