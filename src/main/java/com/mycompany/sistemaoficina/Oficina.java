/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemaoficina;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.sistemaoficina.Estoque.ItemEstoque;

public class Oficina {
    private Elevador elevadores[3];
    private List<Funcionario> funcionarios;
    private List<Cliente> clientes;
    private List<Servico> servicos;
    private Agenda agenda;
    private Estoque estoque;
    private Caixa caixa;
    

    public Oficina() {
        this.funcionarios = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.servicos = new ArrayList<>();
        this.agenda = new Agenda();
        this.caixa = new Caixa();
        this.estoque = new Estoque();
        inicializarServicosPadrao();
    }

    private void inicializarServicosPadrao() {
        servicos.add(new Servico("Troca de Óleo", "Troca de óleo e filtro", 120.00, 30));
        servicos.add(new Servico("Revisão Básica", "Revisão de 10 itens", 250.00, 60));
        servicos.add(new Servico("Revisão Completa", "Revisão de 30 itens", 450.00, 120));
        servicos.add(new Servico("Alinhamento", "Alinhamento e balanceamento", 180.00, 45));
    }

    public void adicionarItemEstoque(String codigo, String nome, int quantidade) {
        estoque.adicionarItem(codigo, nome, quantidade);
    }

    public void removerItemEstoque(String codigo) {
        estoque.removerItem(codigo);
    }

    public void atualizarItemEstoque(String codigo, int novaQuantidade) {
        estoque.atualizarQuantidade(codigo, novaQuantidade);
    }

    public void listarEstoque() {
        estoque.listarItens();
    }

    public Estoque getEstoque() {
        return estoque;
    }

    // Métodos para gerenciar funcionários
    public void contratarFuncionario(Funcionario funcionario) {
        funcionarios.add(funcionario);
        System.out.println("Funcionário " + funcionario.getNome() + " contratado com sucesso!");
    }

    public void demitirFuncionario(String matricula) {
        funcionarios.removeIf(f -> f.getMatricula().equals(matricula));
        System.out.println("Funcionário com matrícula " + matricula + " demitido com sucesso!");
    }

    public void listarFuncionarios() {
        System.out.println("\n=== FUNCIONÁRIOS ===");
        funcionarios.forEach(System.out::println);
    }

    // Métodos para gerenciar clientes
    public void cadastrarCliente(Cliente cliente) {
        clientes.add(cliente);
        System.out.println("Cliente " + cliente.getNome() + " cadastrado com sucesso!");
    }

    public void removerCliente(String telefone) {
        clientes.removeIf(c -> c.getTelefone().equals(telefone));
        System.out.println("Cliente com telefone " + telefone + " removido com sucesso!");
    }

    public void listarClientes() {
        System.out.println("\n=== CLIENTES ===");
        clientes.forEach(System.out::println);
    }

    // Métodos para gerenciar serviços
    public void adicionarServico(Servico servico) {
        servicos.add(servico);
        System.out.println("Serviço " + servico.getNome() + " adicionado com sucesso!");
    }

    public void removerServico(String nomeServico) {
        servicos.removeIf(s -> s.getNome().equalsIgnoreCase(nomeServico));
        System.out.println("Serviço " + nomeServico + " removido com sucesso!");
    }

    public void listarServicos() {
        System.out.println("\n=== SERVIÇOS DISPONÍVEIS ===");
        servicos.forEach(System.out::println);
    }

    // Métodos para gerenciar agendamentos
    public void agendarServico(Cliente cliente, Veiculo veiculo, String nomeServico, Funcionario responsavel, String dataHora, String status) {
        Servico servico = servicos.stream()
            .filter(s -> s.getNome().equalsIgnoreCase(nomeServico))
            .findFirst()
            .orElse(null);
        
        if (servico != null) {
            agenda.adicionarAgendamento(cliente, veiculo, servico, responsavel, dataHora, status);
            System.out.println("Serviço agendado com sucesso!");
        } else {
            System.out.println("Serviço não encontrado!");
        }
    }

    public void listarAgendamentos() {
        agenda.listarAgendamentos();
    }

    // Métodos para gerenciar finanças
    public void registrarPagamento(double valor, String descricao, String data) {
        caixa.registrarEntrada(valor, descricao, data);
    }

    public void registrarDespesa(double valor, String descricao, String data) {
        caixa.registrarSaida(valor, descricao, data);
    }

    public void gerarRelatorioDiario(String data) {
        caixa.gerarRelatorioDiario(data);
    }

    public void gerarRelatorioMensal(int mes, int ano) {
        caixa.gerarRelatorioMensal(mes, ano);
    }

    // Getters
    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public Caixa getCaixa() {
        return caixa;
    }
}