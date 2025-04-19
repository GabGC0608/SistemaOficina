/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaoficina;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;  // Adicione esta linha

public class Administrador extends Funcionario {
    
    public Administrador(  @JsonProperty("nome") String nome,
            @JsonProperty("telefone") String telefone,
            @JsonProperty("endereco") String endereco,@JsonProperty("salario")double salario,@JsonProperty("matricula") String matricula) {
        super(nome, telefone, endereco, "Administrador", salario, matricula);
    }

    public void cadastrarFuncionario(Funcionario novoFuncionario, List<Funcionario> listaFuncionarios) {
        listaFuncionarios.add(novoFuncionario);
        System.out.println("Funcionário " + novoFuncionario.getNome() + " cadastrado com sucesso!");
    }

    public void removerFuncionario(String matricula, List<Funcionario> listaFuncionarios) {
        listaFuncionarios.removeIf(func -> func.getMatricula().equals(matricula));
        System.out.println("Funcionário com matrícula " + matricula + " removido com sucesso!");
    }

    public void ajustarSalario(Funcionario funcionario, double novoSalario) {
        funcionario.setSalario(novoSalario);
        System.out.println("Salário de " + funcionario.getNome() + " ajustado para R$" + novoSalario);
    }

    public void gerarRelatorioGeral(List<Funcionario> funcionarios, List<Cliente> clientes) {
        System.out.println("\n=== RELATÓRIO GERAL ===");
        System.out.println("Total de funcionários: " + funcionarios.size());
        System.out.println("Total de clientes: " + clientes.size());
    }
}