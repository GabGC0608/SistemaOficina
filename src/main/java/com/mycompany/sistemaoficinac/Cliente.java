package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe que representa um cliente da oficina.
 */
public class Cliente extends Pessoa {
    private String clienteId; // ID único do cliente
    private List<Veiculo> veiculos; // Lista de veículos do cliente
    private String cpfAnonimizado; // CPF anonimizado (opcional, para outros fins)
    private static int proximoId = 1; // Contador estático para gerar IDs únicos

    /**
     * Construtor da classe Cliente com lista de veículos.
     *
     * @param nome           Nome do cliente.
     * @param telefone       Telefone do cliente.
     * @param endereco       Endereço do cliente.
     * @param veiculos       Lista de veículos do cliente.
     * @param cpfAnonimizado CPF anonimizado do cliente.
     */
    public Cliente(
            @JsonProperty("nome") String nome,
            @JsonProperty("telefone") String telefone,
            @JsonProperty("endereco") String endereco,
            @JsonProperty(value = "veiculos", required = false) List<Veiculo> veiculos,
            @JsonProperty("cpfAnonimizado") String cpfAnonimizado) {
        super(nome, telefone, endereco);
        this.clienteId = gerarIdUnico(); // Gera um ID único ao criar o cliente
        this.veiculos = veiculos != null ? veiculos : new ArrayList<>();
        this.cpfAnonimizado = cpfAnonimizado;
    }

    /**
     * Construtor da classe Cliente sem lista de veículos.
     *
     * @param nome           Nome do cliente.
     * @param telefone       Telefone do cliente.
     * @param endereco       Endereço do cliente.
     * @param cpfAnonimizado CPF anonimizado do cliente.
     */
    public Cliente(String nome, String telefone, String endereco, String cpfAnonimizado) {
        this(nome, telefone, endereco, null, cpfAnonimizado);
    }

    /**
     * Gera um ID único para o cliente.
     *
     * @return ID único do cliente.
     */
    private String gerarIdUnico() {
        return "CLI-" + String.format("%04d", proximoId++); // Formato: CLI-0001, CLI-0002, ...
    }

    /**
     * Obtém o ID único do cliente.
     *
     * @return ID único do cliente.
     */
    public String getClienteId() {
        return clienteId;
    }

    /**
     * Define o ID único do cliente (pode ser útil para carregar dados).
     *
     * @param clienteId ID único do cliente.
     */
    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    // Getters e setters para outros atributos (veiculos, cpfAnonimizado, etc.)

    /**
     * Adiciona um veículo à lista de veículos do cliente.
     *
     * @param veiculo Veículo a ser adicionado.
     */
    public void adicionarVeiculo(Veiculo veiculo) {
        veiculos.add(veiculo);
        System.out.println("Veículo " + veiculo.getModelo() + " adicionado ao cliente " + getNome());
    }

    /**
     * Remove um veículo da lista de veículos do cliente.
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
     * Define a lista de veículos do cliente.
     *
     * @param veiculos Lista de veículos.
     */
    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    /**
     * Obtém o CPF anonimizado do cliente.
     *
     * @return CPF anonimizado do cliente.
     */
    public String getCpfAnonimizado() {
        return cpfAnonimizado;
    }

    /**
     * Define o CPF anonimizado do cliente.
     *
     * @param cpfAnonimizado CPF anonimizado do cliente.
     */
    public void setCpfAnonimizado(String cpfAnonimizado) {
        this.cpfAnonimizado = cpfAnonimizado;
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

    @Override
    public String toString() {
        return "Cliente{" +
                "clienteId='" + clienteId + '\'' +
                ", nome='" + getNome() + '\'' +
                ", telefone='" + getTelefone() + '\'' +
                ", endereco='" + getEndereco() + '\'' +
                ", cpfAnonimizado='" + cpfAnonimizado + '\'' +
                '}';
    }
}