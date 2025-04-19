/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template

*/

package com.mycompany.sistemaoficina;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Funcionario extends Pessoa {
    private String cargo;
    private double salario;
    private String matricula;

    public Funcionario(@JsonProperty("nome")String nome,@JsonProperty("telefone") String telefone,@JsonProperty("endereco") String endereco,@JsonProperty("cargo") String cargo,@JsonProperty("salario") double salario,@JsonProperty("matricula") String matricula) {
        super(nome, telefone, endereco);
        this.cargo = cargo;
        this.salario = salario;
        this.matricula = matricula;
    }

    // Getters e Setters
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getMatricula() {
        return matricula;
    }

    // Métodos específicos
    public void realizarAgendamento(Cliente cliente, Veiculo veiculo, Servico servico, String dataHora) {
        System.out.println("Agendamento realizado para " + cliente.getNome() + 
                         " - Veículo: " + veiculo.getModelo() + 
                         " - Serviço: " + servico.toString() + 
                         " - Data/Hora: " + dataHora);
    }

    public void gerarRelatorioServico() {
        System.out.println("Relatório de serviços gerado por " + getNome());
    }

    @Override
    public String toString() {
        return super.toString() + ", Cargo: " + cargo + ", Matrícula: " + matricula + ", Salário: R$" + salario;
    }
}