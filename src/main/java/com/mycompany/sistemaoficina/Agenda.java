/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaoficina;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private List<Agendamento> agendamentos = new ArrayList<>();

    // Adicione este método getter
    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public static class Agendamento {
        
        private Cliente cliente;
        private Veiculo veiculo;
        private Servico servico;
        private Funcionario responsavel;
        private String dataHora;
        private String status;

        public Agendamento(@JsonProperty("cliente")Cliente cliente, @JsonProperty("veiculo")Veiculo veiculo,@JsonProperty("servico") Servico servico,@JsonProperty("responsavel") Funcionario responsavel,@JsonProperty("dataHora") String dataHora,@JsonProperty("status") String status) {
            this.cliente = cliente;
            this.veiculo = veiculo;
            this.servico = servico;
            this.responsavel = responsavel;
            this.dataHora = dataHora;
            this.status = status;
        }

        
        
        // Adicione estes métodos getters
        public Cliente getCliente() {
            return cliente;
        }

        public Veiculo getVeiculo() {
            return veiculo;
        }

        public Servico getServico() {
            return servico;
        }

        public String getDataHora() {
            return dataHora;
        }

        // Getters e Setters
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Agendamento: " + dataHora + 
                   "\nCliente: " + cliente.getNome() +
                   "\nVeículo: " + veiculo.getModelo() + " (" + veiculo.getPlaca() + ")" +
                   "\nServiço: " + servico.getNome() + 
                   "\nResponsável: " + responsavel.getNome() +
                   "\nStatus: " + status + "\n";
        }
    }

    public void adicionarAgendamento(Cliente cliente, Veiculo veiculo, Servico servico, 
                                   Funcionario responsavel, String dataHora, String status) {
        Agendamento novoAgendamento = new Agendamento(cliente, veiculo, servico, responsavel, dataHora, status);
        agendamentos.add(novoAgendamento);
        System.out.println("Agendamento realizado com sucesso!");
    }

    public void cancelarAgendamento(int index) {
        if (index >= 0 && index < agendamentos.size()) {
            agendamentos.get(index).setStatus("Cancelado");
            System.out.println("Agendamento cancelado com sucesso!");
        } else {
            System.out.println("Índice de agendamento inválido!");
        }
    }

    public void concluirAgendamento(int index) {
        if (index >= 0 && index < agendamentos.size()) {
            agendamentos.get(index).setStatus("Concluído");
            System.out.println("Agendamento marcado como concluído!");
        } else {
            System.out.println("Índice de agendamento inválido!");
        }
    }

    public void listarAgendamentos() {
        System.out.println("\n=== LISTA DE AGENDAMENTOS ===");
        for (int i = 0; i < agendamentos.size(); i++) {
            System.out.println("[" + i + "] " + agendamentos.get(i));
        }
    }

    public void listarAgendamentosPorCliente(String nomeCliente) {
        System.out.println("\n=== AGENDAMENTOS DO CLIENTE " + nomeCliente.toUpperCase() + " ===");
        agendamentos.stream()
            .filter(a -> a.cliente.getNome().equalsIgnoreCase(nomeCliente))
            .forEach(System.out::println);
    }

    public void listarAgendamentosPorStatus(String status) {
        System.out.println("\n=== AGENDAMENTOS COM STATUS " + status.toUpperCase() + " ===");
        agendamentos.stream()
            .filter(a -> a.status.equalsIgnoreCase(status))
            .forEach(System.out::println);
    }
}