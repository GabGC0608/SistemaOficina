package com.mycompany.sistemaoficina;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;

public class Cliente extends Pessoa {
    private List<Veiculo> veiculos;

    public Cliente(@JsonProperty("nome") String nome,
                   @JsonProperty("telefone") String telefone,
                   @JsonProperty("endereco") String endereco,
                   @JsonProperty(value = "veiculos", required = false) List<Veiculo> veiculos) {
        super(nome, telefone, endereco);
        this.veiculos = (veiculos != null) ? veiculos : new ArrayList<>();
    }

    public Cliente(String nome, String telefone, String endereco) {
        super(nome, telefone, endereco);
        this.veiculos = new ArrayList<>();
    }

    public void adicionarVeiculo(Veiculo veiculo) {
        this.veiculos.add(veiculo);
        System.out.println("Veículo " + veiculo.getModelo() + " adicionado ao cliente " + getNome());
    }

    public void removerVeiculo(String placa) {
        boolean removido = veiculos.removeIf(v -> v.getPlaca().equals(placa));
        if (removido) {
            System.out.println("Veículo com placa " + placa + " removido do cliente " + getNome());
        } else {
            System.out.println("Nenhum veículo com placa " + placa + " encontrado para o cliente " + getNome());
        }
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void listarVeiculos() {
        System.out.println("\nVeículos do cliente " + getNome() + ":");
        if (veiculos.isEmpty()) {
            System.out.println("Nenhum veículo cadastrado.");
        } else {
            for (Veiculo v : veiculos) {
                System.out.println(v);
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", Quantidade de veículos: " + veiculos.size();
    }
}