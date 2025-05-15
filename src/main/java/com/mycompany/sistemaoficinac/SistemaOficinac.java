package com.mycompany.sistemaoficinac;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class SistemaOficinac {
    private static final Oficina oficina = new Oficina();
    private static final Scanner scanner = new Scanner(System.in);
    private static Login loginManager = new Login();

    public static void main(String[] args) {
        if (!realizarLogin()) {
            System.out.println("Acesso negado. Encerrando...");
            return;
        }

        boolean executando = true;
        while (executando) {
            exibirMenuPrincipal();
            int opcao = lerInteiro("Opção: ");
            
            switch (opcao) {
                case 1 -> menuClientes();
                case 2 -> menuAgendamentos();
                case 3 -> menuEstoque();
                case 4 -> menuFinanceiro();
                case 0 -> executando = false;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    // ================== MENUS ==================
    private static void exibirMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Gerenciar Clientes");
        System.out.println("2. Gerenciar Agendamentos");
        System.out.println("3. Gerenciar Estoque");
        System.out.println("4. Gerenciar Financeiro");
        System.out.println("0. Sair");
    }

    private static void menuClientes() {
        System.out.println("\n=== CLIENTES ===");
        System.out.println("1. Cadastrar");
        System.out.println("2. Editar");
        System.out.println("3. Listar");
        System.out.println("4. Remover");
        
        switch (lerInteiro("Opção: ")) {
            case 1 -> cadastrarCliente();
            case 2 -> editarCliente();
            case 3 -> listarClientes();
            case 4 -> removerCliente();
        }
    }

    // ================== OPERAÇÕES DE CLIENTE ==================
    private static void cadastrarCliente() {
        System.out.println("\n=== CADASTRO CLIENTE ===");
        String nome = lerString("Nome: ");
        String telefone = lerString("Telefone: ");
        String endereco = lerString("Endereço: ");
        String cpf = lerString("CPF: ");
        
        oficina.cadastrarCliente(nome, telefone, endereco, cpf);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private static void editarCliente() {
        String telefone = lerString("Telefone do cliente: ");
        String novoNome = lerString("Novo nome (enter para manter): ");
        String novoTelefone = lerString("Novo telefone (enter para manter): ");
        
        oficina.alterarDadosCliente(telefone, 
            novoNome.isEmpty() ? null : novoNome,
            novoTelefone.isEmpty() ? null : novoTelefone,
            null);
    }

    // ================== HELPERS ==================
    private static boolean realizarLogin() {
        System.out.println("=== LOGIN ===");
        String usuario = lerString("Usuário: ");
        String senha = lerString("Senha: ");
        return loginManager.login(usuario, senha) != null;
    }

    private static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido!");
            }
        }
    }
}