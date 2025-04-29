package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que gerencia os agendamentos da oficina.
 */
public class Agenda {
    private List<Agendamento> agendamentos = new ArrayList<>();

    /**
     * Obtém a lista de agendamentos.
     *
     * @return Lista de agendamentos.
     */
    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    /**
     * Define a lista de agendamentos.
     *
     * @param agendamentos Nova lista de agendamentos.
     */
    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }

    /**
     * Classe interna que representa um agendamento.
     */
    public static class Agendamento {
        private Cliente cliente; // Cliente relacionado ao agendamento
        private Veiculo veiculo; // Veículo relacionado ao agendamento
        private Servico servico; // Serviço a ser realizado
        private Funcionario responsavel; // Funcionário responsável pelo serviço
        private String dataHora; // Data e hora do agendamento
        private String status; // Status do agendamento (ex.: "Pendente", "Concluído")

        /**
         * Construtor da classe Agendamento.
         *
         * @param cliente Cliente do agendamento.
         * @param veiculo Veículo do agendamento.
         * @param servico Serviço a ser realizado.
         * @param responsavel Funcionário responsável.
         * @param dataHora Data e hora do agendamento.
         * @param status Status do agendamento.
         */
        @JsonCreator
        public Agendamento(@JsonProperty("cliente") Cliente cliente,
                           @JsonProperty("veiculo") Veiculo veiculo,
                           @JsonProperty("servico") Servico servico,
                           @JsonProperty("responsavel") Funcionario responsavel,
                           @JsonProperty("dataHora") String dataHora,
                           @JsonProperty("status") String status) {
            this.cliente = cliente;
            this.veiculo = veiculo;
            this.servico = servico;
            this.responsavel = responsavel;
            this.dataHora = dataHora;
            this.status = status;
        }

        /**
         * Obtém o cliente do agendamento.
         *
         * @return Cliente do agendamento.
         */
        public Cliente getCliente() {
            return cliente;
        }

        /**
         * Define o cliente do agendamento.
         *
         * @param cliente Novo cliente do agendamento.
         */
        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
        }

        /**
         * Obtém o veículo do agendamento.
         *
         * @return Veículo do agendamento.
         */
        public Veiculo getVeiculo() {
            return veiculo;
        }

        /**
         * Define o veículo do agendamento.
         *
         * @param veiculo Novo veículo do agendamento.
         */
        public void setVeiculo(Veiculo veiculo) {
            this.veiculo = veiculo;
        }

        /**
         * Obtém o serviço do agendamento.
         *
         * @return Serviço do agendamento.
         */
        public Servico getServico() {
            return servico;
        }

        /**
         * Define o serviço do agendamento.
         *
         * @param servico Novo serviço do agendamento.
         */
        public void setServico(Servico servico) {
            this.servico = servico;
        }

        /**
         * Obtém o funcionário responsável pelo agendamento.
         *
         * @return Funcionário responsável pelo agendamento.
         */
        public Funcionario getResponsavel() {
            return responsavel;
        }

        /**
         * Define o funcionário responsável pelo agendamento.
         *
         * @param responsavel Novo funcionário responsável pelo agendamento.
         */
        public void setResponsavel(Funcionario responsavel) {
            this.responsavel = responsavel;
        }

        /**
         * Obtém a data e hora do agendamento.
         *
         * @return Data e hora do agendamento.
         */
        public String getDataHora() {
            return dataHora;
        }

        /**
         * Define a data e hora do agendamento.
         *
         * @param dataHora Nova data e hora do agendamento.
         */
        public void setDataHora(String dataHora) {
            this.dataHora = dataHora;
        }

        /**
         * Obtém o status do agendamento.
         *
         * @return Status do agendamento.
         */
        public String getStatus() {
            return status;
        }

        /**
         * Define o status do agendamento.
         *
         * @param status Novo status do agendamento.
         */
        public void setStatus(String status) {
            this.status = status;
        }
    }

    /**
     * Adiciona um novo agendamento à lista.
     *
     * @param cliente Cliente do agendamento.
     * @param veiculo Veículo do agendamento.
     * @param servico Serviço a ser realizado.
     * @param responsavel Funcionário responsável.
     * @param dataHora Data e hora do agendamento.
     * @param status Status do agendamento.
     */
    public void adicionarAgendamento(Cliente cliente, Veiculo veiculo, Servico servico,
                                     Funcionario responsavel, String dataHora, String status) {
        agendamentos.add(new Agendamento(cliente, veiculo, servico, responsavel, dataHora, status));
        System.out.println("Agendamento adicionado com sucesso!");
    }

    /**
     * Cancela um agendamento com base no índice.
     *
     * @param index Índice do agendamento a ser cancelado.
     */
    public void cancelarAgendamento(int index) {
        if (index >= 0 && index < agendamentos.size()) {
            agendamentos.remove(index);
            System.out.println("Agendamento cancelado com sucesso!");
        } else {
            System.out.println("Índice inválido!");
        }
    }

    /**
     * Conclui um agendamento com base no índice.
     *
     * @param index Índice do agendamento a ser concluído.
     */
    public void concluirAgendamento(int index) {
        if (index >= 0 && index < agendamentos.size()) {
            agendamentos.get(index).status = "Concluído";
            System.out.println("Agendamento concluído com sucesso!");
        } else {
            System.out.println("Índice inválido!");
        }
    }

    /**
     * Lista todos os agendamentos.
     */
    public void listarAgendamentos() {
        System.out.println("\n=== LISTA DE AGENDAMENTOS ===");
        for (int i = 0; i < agendamentos.size(); i++) {
            System.out.println("Agendamento " + (i + 1) + ": " + agendamentos.get(i));
        }
    }

    /**
     * Lista os agendamentos de um cliente específico.
     *
     * @param nomeCliente Nome do cliente.
     */
    public void listarAgendamentosPorCliente(String nomeCliente) {
        System.out.println("\n=== AGENDAMENTOS DO CLIENTE: " + nomeCliente + " ===");
        for (Agendamento agendamento : agendamentos) {
            if (agendamento.cliente.getNome().equalsIgnoreCase(nomeCliente)) {
                System.out.println(agendamento);
            }
        }
    }

    /**
     * Lista os agendamentos com base no status.
     *
     * @param status Status dos agendamentos a serem listados.
     */
    public void listarAgendamentosPorStatus(String status) {
        System.out.println("\n=== AGENDAMENTOS COM STATUS: " + status + " ===");
        for (Agendamento agendamento : agendamentos) {
            if (agendamento.status.equalsIgnoreCase(status)) {
                System.out.println(agendamento);
            }
        }
    }
}