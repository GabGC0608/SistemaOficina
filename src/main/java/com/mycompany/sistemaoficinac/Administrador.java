package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;

/**
 * Classe que representa um administrador, que é um tipo de funcionário.
 */
@JsonTypeName("administrador")
public class Administrador extends Funcionario {

    /**
     * Construtor da classe Administrador.
     *
     * @param nome Nome do administrador.
     * @param telefone Telefone do administrador.
     * @param endereco Endereço do administrador.
     * @param salario Salário do administrador.
     * @param matricula Matrícula do administrador.
     */
    @JsonCreator
    public Administrador(@JsonProperty("nome") String nome,
                         @JsonProperty("telefone") String telefone,
                         @JsonProperty("endereco") String endereco,
                         @JsonProperty("salario") double salario,
                         @JsonProperty("matricula") String matricula) {
        super(nome, telefone, endereco, "Administrador", salario, matricula, "Administração");
    }

    // Construtor vazio para o Jackson
    protected Administrador() {
        super();
    }

    /**
     * Cadastra um novo funcionário na lista de funcionários.
     *
     * @param novoFuncionario Funcionário a ser cadastrado.
     * @param listaFuncionarios Lista de funcionários.
     */
    public void cadastrarFuncionario(Funcionario novoFuncionario, List<Funcionario> listaFuncionarios) {
        listaFuncionarios.add(novoFuncionario);
        System.out.println("Funcionário " + novoFuncionario.getNome() + " cadastrado com sucesso!");
    }

    /**
     * Remove um funcionário da lista de funcionários com base na matrícula.
     *
     * @param matricula Matrícula do funcionário a ser removido.
     * @param listaFuncionarios Lista de funcionários.
     */
    public void removerFuncionario(String matricula, List<Funcionario> listaFuncionarios) {
        listaFuncionarios.removeIf(func -> func.getMatricula().equals(matricula));
        System.out.println("Funcionário com matrícula " + matricula + " removido com sucesso!");
    }

    /**
     * Ajusta o salário de um funcionário.
     *
     * @param funcionario Funcionário cujo salário será ajustado.
     * @param novoSalario Novo valor do salário.
     */
    public void ajustarSalario(Funcionario funcionario, double novoSalario) {
        funcionario.setSalario(novoSalario);
        System.out.println("Salário de " + funcionario.getNome() + " ajustado para R$" + novoSalario);
    }

    /**
     * Gera um relatório geral com o total de funcionários e clientes.
     *
     * @param funcionarios Lista de funcionários.
     * @param clientes Lista de clientes.
     */
    public void gerarRelatorioGeral(List<Funcionario> funcionarios, List<Cliente> clientes) {
        System.out.println("\n=== RELATÓRIO GERAL ===");
        System.out.println("Total de funcionários: " + funcionarios.size());
        System.out.println("Total de clientes: " + clientes.size());
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "nome='" + getNome() + '\'' +
                ", telefone='" + getTelefone() + '\'' +
                ", endereco='" + getEndereco() + '\'' +
                ", cargo='" + getCargo() + '\'' +
                ", salario=" + getSalario() +
                ", matricula='" + getMatricula() + '\'' +
                ", departamento='" + getDepartamento() + '\'' +
                '}';
    }
}