package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    public boolean verificarDisponibilidadeFuncionario(Funcionario funcionario, String dataHora) {
        for (Agendamento agendamento : agendamentos) {
            if (agendamento.getResponsavel().equals(funcionario)) {
                if (agendamento.getDataHora().equals(dataHora)) {
                    return false; // Funcionário já tem um agendamento neste horário
                }
            }
        }
        return true; // Funcionário está disponível
    }

    /**
     * Classe interna que representa um agendamento.
     */
    public static class Agendamento {
        private Cliente cliente; // Cliente relacionado ao agendamento
        private Veiculo veiculo; // Veículo relacionado ao agendamento
         private List<Servico> servicos; // Serviço a ser realizado
        private Funcionario responsavel; // Funcionário responsável pelo serviço
        private String dataHora; // Data e hora do agendamento
        private String status; // Status do agendamento (ex.: "Pendente", "Concluído")
        private double valorTotal; 

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
                           @JsonProperty("servicos") List<Servico> servicos,
                           @JsonProperty("responsavel") Funcionario responsavel,
                           @JsonProperty("dataHora") String dataHora,
                           @JsonProperty("status") String status) {
            this.cliente = cliente;
            this.veiculo = veiculo;
            this.servicos = servicos;
            this.responsavel = responsavel;
            this.dataHora = dataHora;
            this.status = status;
            this.valorTotal = calcularValorTotal();
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
        /***
         * Calcula o valor total do agendamento somando os valores dos serviços.
         * @return Valor total do agendamento.
         */
        private double calcularValorTotal() {
            return servicos.stream().mapToDouble(Servico::getValor).sum();
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
        public List<Servico> getServicos() {
            return servicos;
        }

        /**
         * Define o serviço do agendamento.
         *
         * @param servico Novo serviço do agendamento.
         * 
         */
        public void setServicos(List<Servico> servicos) {
            this.servicos = servicos;
            this.valorTotal = calcularValorTotal();
            
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
        /***
         * Obtém o valor total do agendamento.
         * @return
         */
        public double getValorTotal() {
            return servicos.stream().mapToDouble(Servico::getValor).sum();
        }
            /***
             * Retorna uma representação em string do agendamento.
             */
        @Override
        public String toString() {
            String servicosStr = servicos.stream()
                .map(Servico::getNome)
                .collect(Collectors.joining(", "));
            
            return String.format("Cliente: %s | Veículo: %s | Serviços: %s | Responsável: %s | Data/Hora: %s | Status: %s | Valor Total: R$%.2f",
                    cliente.getNome(),
                    veiculo.getModelo(),
                    servicosStr,
                    responsavel.getNome(),
                    dataHora,
                    status,
                    getValorTotal());
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
    public void adicionarAgendamento(Cliente cliente, Veiculo veiculo, List<Servico> servicos, Funcionario responsavel, String dataHora, String status) {
        agendamentos.add(new Agendamento(cliente, veiculo, servicos, responsavel, dataHora, status));
        System.out.println("Agendamento adicionado com sucesso!");
    }

        /**
     * Cancela um agendamento com base no índice, retendo 20% do valor do serviço.
     *
     * @param index Índice do agendamento a ser cancelado.
     * @return Valor retido (20% do valor do serviço).
     */
    public double cancelarAgendamento(int index) {
        if (index >= 0 && index < agendamentos.size()) {
            Agendamento agendamento = agendamentos.get(index);
            double valorTotal = agendamento.getValorTotal();
            double valorRetido = valorTotal * 0.20;

            System.out.println("Cancelando o agendamento...");
            System.out.printf("Valor total dos serviços: R$ %.2f%n", valorTotal);
            System.out.printf("Valor retido (20%%): R$ %.2f%n", valorRetido);

            agendamentos.remove(index);
            System.out.println("Agendamento cancelado com sucesso!");

            return valorRetido;
        } else {
            System.out.println("Índice inválido!");
            return 0;
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