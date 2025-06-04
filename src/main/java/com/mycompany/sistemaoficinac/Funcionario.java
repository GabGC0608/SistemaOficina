package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Classe que representa um funcionário da oficina.
 * Herda da classe Pessoa.
 */

public class Funcionario extends Pessoa {
    @JsonProperty("cargo")
    private String cargo; // Cargo do funcionário
    @JsonProperty("salario")
    private double salario; // Salário do funcionário
    @JsonProperty("matricula")
    private String matricula; // Matrícula do funcionário
    @JsonProperty("departamento")
    private String departamento; // Departamento do funcionário

    /**
     * Construtor da classe Funcionario.
     *
     * @param nome Nome do funcionário.
     * @param telefone Telefone do funcionário.
     * @param endereco Endereço do funcionário.
     * @param cargo Cargo do funcionário.
     * @param salario Salário do funcionário.
     * @param matricula Matrícula do funcionário.
     * @param departamento Departamento do funcionário.
     */
    @JsonCreator
    public Funcionario(@JsonProperty("nome") String nome,
                      @JsonProperty("telefone") String telefone,
                      @JsonProperty("endereco") String endereco,
                      @JsonProperty("cargo") String cargo,
                      @JsonProperty("salario") double salario,
                      @JsonProperty("matricula") String matricula,
                      @JsonProperty("departamento") String departamento) {
        super(nome, telefone, endereco);
        this.cargo = cargo;
        this.salario = salario;
        this.matricula = matricula;
        this.departamento = departamento;
    }

    // Construtor vazio para o Jackson
    protected Funcionario() {
        super();
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
     * Define a matrícula do funcionário.
     *
     * @param matricula Nova matrícula do funcionário.
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * Obtém o departamento do funcionário.
     *
     * @return Departamento do funcionário.
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * Define o departamento do funcionário.
     *
     * @param departamento Novo departamento do funcionário.
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    /**
     * Realiza uma inspeção no veículo e retorna o resultado.
     *
     * @param veiculo Veículo a ser inspecionado.
     * @return Resultado da inspeção com a especialidade necessária.
     */
    public String realizarInspecao(Veiculo veiculo) {
        System.out.println(this.getNome() + " está realizando a inspeção do veículo: " + veiculo.getModelo());
        // Aqui seria implementada a lógica real de inspeção
        // Por enquanto, retornamos um resultado simulado
        return "Inspeção realizada por " + this.getNome() + " (" + this.getCargo() + ")";
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
        return "Funcionario{" +
                "nome='" + getNome() + '\'' +
                ", telefone='" + getTelefone() + '\'' +
                ", endereco='" + getEndereco() + '\'' +
                ", cargo='" + cargo + '\'' +
                ", salario=" + salario +
                ", matricula='" + matricula + '\'' +
                ", departamento='" + departamento + '\'' +
                '}';
    }
}