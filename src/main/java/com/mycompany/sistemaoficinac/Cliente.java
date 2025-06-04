package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe que representa um cliente da oficina.
 */

public class Cliente extends Pessoa {
    @JsonProperty("clienteId")
    private String clienteId; // ID único do cliente
    @JsonProperty("veiculos")
    private List<Veiculo> veiculos; // Lista de veículos do cliente
    @JsonProperty("cpfAnonimizado")
    private String cpfAnonimizado; // CPF anonimizado (opcional, para outros fins)
    private static int proximoId = 1; // Contador estático para gerar IDs únicos

    /**
     * Construtor completo para o Jackson
     */
    @JsonCreator
    public Cliente(@JsonProperty("nome") String nome,
                  @JsonProperty("telefone") String telefone,
                  @JsonProperty("endereco") String endereco,
                  @JsonProperty("clienteId") String clienteId,
                  @JsonProperty("veiculos") List<Veiculo> veiculos,
                  @JsonProperty("cpfAnonimizado") String cpfAnonimizado) {
        super(nome, telefone, endereco);
        this.clienteId = clienteId != null ? clienteId : gerarIdUnico();
        this.veiculos = veiculos != null ? veiculos : new ArrayList<>();
        this.cpfAnonimizado = cpfAnonimizado;
    }

    /**
     * Construtor para criar um novo cliente
     */
    public Cliente(String nome, String telefone, String endereco, String cpfAnonimizado) {
        super(nome, telefone, endereco);
        this.clienteId = gerarIdUnico();
        this.veiculos = new ArrayList<>();
        this.cpfAnonimizado = cpfAnonimizado;
    }

   

    /**
     * Gera um ID único para o cliente.
     */
    private String gerarIdUnico() {
        return "CLI" + String.format("%04d", proximoId++);
    }

    // Getters e Setters

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    public String getCpfAnonimizado() {
        return cpfAnonimizado;
    }

    public void setCpfAnonimizado(String cpfAnonimizado) {
        this.cpfAnonimizado = cpfAnonimizado;
    }

    /**
     * Adiciona um veículo à lista de veículos do cliente.
     */
    public void adicionarVeiculo(Veiculo veiculo) {
        if (veiculos == null) {
            veiculos = new ArrayList<>();
        }
        veiculos.add(veiculo);
        System.out.println("Veículo " + veiculo.getModelo() + " adicionado ao cliente " + getNome());
    }

    /**
     * Remove um veículo da lista de veículos do cliente.
     */
    public void removerVeiculo(String placa) {
        if (veiculos != null) {
            veiculos.removeIf(veiculo -> veiculo.getPlaca().equalsIgnoreCase(placa));
            System.out.println("Veículo removido com sucesso!");
        }
    }

    /**
     * Lista todos os veículos do cliente.
     */
    public void listarVeiculos() {
        System.out.println("\n=== VEÍCULOS DO CLIENTE ===");
        if (veiculos != null) {
            for (Veiculo veiculo : veiculos) {
                System.out.println(veiculo);
            }
        } else {
            System.out.println("Este cliente não possui veículos cadastrados.");
        }
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "clienteId='" + clienteId + '\'' +
                ", nome='" + getNome() + '\'' +
                ", telefone='" + getTelefone() + '\'' +
                ", endereco='" + getEndereco() + '\'' +
                ", veiculos=" + veiculos +
                ", cpfAnonimizado='" + cpfAnonimizado + '\'' +
                '}';
    }
}