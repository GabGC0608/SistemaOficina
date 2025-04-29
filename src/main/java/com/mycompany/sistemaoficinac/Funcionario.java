package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe que representa um funcionário da oficina.
 * Herda da classe Pessoa.
 */
public class Funcionario extends Pessoa {
    private String cargo; // Cargo do funcionário
    private double salario; // Salário do funcionário
    private String matricula; // Matrícula do funcionário

    /**
     * Construtor da classe Funcionario.
     *
     * @param nome Nome do funcionário.
     * @param telefone Telefone do funcionário.
     * @param endereco Endereço do funcionário.
     * @param cargo Cargo do funcionário.
     * @param salario Salário do funcionário.
     * @param matricula Matrícula do funcionário.
     */
    public Funcionario(@JsonProperty("nome") String nome,
                       @JsonProperty("telefone") String telefone,
                       @JsonProperty("endereco") String endereco,
                       @JsonProperty("cargo") String cargo,
                       @JsonProperty("salario") double salario,
                       @JsonProperty("matricula") String matricula) {
        super(nome, telefone, endereco);
        this.cargo = cargo;
        this.salario = salario;
        this.matricula = matricula;
    }

    /**
     * Obtém o cargo do funcionário.
     *
     * @return Cargo do funcionário.
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * Define o cargo do funcionário.
     *
     * @param cargo Novo cargo do funcionário.
     */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    /**
     * Obtém o salário do funcionário.
     *
     * @return Salário do funcionário.
     */
    public double getSalario() {
        return salario;
    }

    /**
     * Define o salário do funcionário.
     *
     * @param salario Novo salário do funcionário.
     */
    public void setSalario(double salario) {
        this.salario = salario;
    }

    /**
     * Obtém a matrícula do funcionário.
     *
     * @return Matrícula do funcionário.
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Realiza um agendamento para um cliente.
     *
     * @param cliente Cliente para o qual o agendamento será realizado.
     * @param veiculo Veículo relacionado ao agendamento.
     * @param servico Serviço a ser realizado.
     * @param dataHora Data e hora do agendamento.
     */
    public void realizarAgendamento(Cliente cliente, Veiculo veiculo, Servico servico, String dataHora) {
        System.out.println("Agendamento realizado para " + cliente.getNome() +
                         " - Veículo: " + veiculo.getModelo() +
                         " - Serviço: " + servico.toString() +
                         " - Data/Hora: " + dataHora);
    }

    /**
     * Gera um relatório de serviços realizados pelo funcionário.
     */
    public void gerarRelatorioServico() {
        System.out.println("Relatório de serviços gerado por " + getNome());
    }

    /**
     * Retorna uma representação textual do funcionário.
     *
     * @return String representando o funcionário.
     */
    @Override
    public String toString() {
        return super.toString() + ", Cargo: " + cargo + ", Matrícula: " + matricula + ", Salário: R$" + salario;
    }
}