package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.List;

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
    @JsonProperty("especialidades")
    private List<String> especialidades; // Lista de especialidades do funcionário
    @JsonProperty("isEspecialista")
    private boolean isEspecialista; // Indica se o funcionário é especialista

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
     * @param especialidades Lista de especialidades do funcionário.
     * @param isEspecialista Indica se o funcionário é especialista.
     */
    @JsonCreator
    public Funcionario(@JsonProperty("nome") String nome,
                      @JsonProperty("telefone") String telefone,
                      @JsonProperty("endereco") String endereco,
                      @JsonProperty("cargo") String cargo,
                      @JsonProperty("salario") double salario,
                      @JsonProperty("matricula") String matricula,
                      @JsonProperty("departamento") String departamento,
                      @JsonProperty("especialidades") List<String> especialidades,
                      @JsonProperty("isEspecialista") boolean isEspecialista) {
        super(nome, telefone, endereco);
        this.cargo = cargo;
        this.salario = salario;
        this.matricula = matricula;
        this.departamento = departamento;
        this.especialidades = especialidades != null ? especialidades : new ArrayList<>();
        this.isEspecialista = isEspecialista;
    }

    // Construtor vazio para o Jackson
    protected Funcionario() {
        super();
        this.especialidades = new ArrayList<>();
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
     * Obtém a lista de especialidades do funcionário.
     *
     * @return Lista de especialidades.
     */
    public List<String> getEspecialidades() {
        return new ArrayList<>(especialidades);
    }

    /**
     * Define a lista de especialidades do funcionário.
     *
     * @param especialidades Nova lista de especialidades.
     */
    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades != null ? especialidades : new ArrayList<>();
    }

    /**
     * Verifica se o funcionário é especialista.
     *
     * @return true se for especialista, false caso contrário.
     */
    public boolean isEspecialista() {
        return isEspecialista;
    }

    /**
     * Define se o funcionário é especialista.
     *
     * @param isEspecialista Novo status de especialista.
     */
    public void setEspecialista(boolean isEspecialista) {
        this.isEspecialista = isEspecialista;
    }

    /**
     * Adiciona uma especialidade ao funcionário.
     *
     * @param especialidade Especialidade a ser adicionada.
     */
    public void adicionarEspecialidade(String especialidade) {
        if (especialidade != null && !especialidade.trim().isEmpty() && !especialidades.contains(especialidade)) {
            especialidades.add(especialidade);
        }
    }

    /**
     * Remove uma especialidade do funcionário.
     *
     * @param especialidade Especialidade a ser removida.
     */
    public void removerEspecialidade(String especialidade) {
        especialidades.remove(especialidade);
    }

    /**
     * Verifica se o funcionário possui uma determinada especialidade.
     *
     * @param especialidade Especialidade a ser verificada.
     * @return true se possuir a especialidade, false caso contrário.
     */
    public boolean possuiEspecialidade(String especialidade) {
        return especialidades.contains(especialidade);
    }

    /**
     * Realiza uma inspeção no veículo e retorna o resultado.
     *
     * @param veiculo Veículo a ser inspecionado.
     * @return Resultado da inspeção com a especialidade necessária.
     */
    public String realizarInspecao(Veiculo veiculo) {
        System.out.println(this.getNome() + " está realizando a inspeção do veículo: " + veiculo.getModelo());
        
        // Simula a detecção de problemas e determina a especialidade necessária
        String[] problemasPossiveis = {
            "Problema no motor",
            "Problema na suspensão",
            "Problema elétrico",
            "Problema no sistema de freios",
            "Problema na transmissão"
        };
        
        String problema = problemasPossiveis[(int) (Math.random() * problemasPossiveis.length)];
        String especialidadeNecessaria = determinarEspecialidadeNecessaria(problema);
        
        return String.format("Inspeção realizada por %s (%s). Problema detectado: %s. Especialidade necessária: %s",
            this.getNome(), this.getCargo(), problema, especialidadeNecessaria);
    }

    /**
     * Determina a especialidade necessária com base no problema detectado.
     *
     * @param problema Problema detectado na inspeção.
     * @return Especialidade necessária para resolver o problema.
     */
    private String determinarEspecialidadeNecessaria(String problema) {
        switch (problema) {
            case "Problema no motor":
                return "Mecânico de Motor";
            case "Problema na suspensão":
                return "Mecânico de Suspensão";
            case "Problema elétrico":
                return "Mecânico Elétrico";
            case "Problema no sistema de freios":
                return "Mecânico de Freios";
            case "Problema na transmissão":
                return "Mecânico de Transmissão";
            default:
                return "Mecânico Geral";
        }
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
                ", especialidades=" + especialidades +
                ", isEspecialista=" + isEspecialista +
                '}';
    }
}