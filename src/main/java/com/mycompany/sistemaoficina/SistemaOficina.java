/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemaoficina;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Scanner;

public class SistemaOficina {
    private static final String ARQUIVO_DADOS = "data/ficina.json";
    private static Oficina oficina = new Oficina();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        carregarDados();
        System.out.println("=== SISTEMA DE GERENCIAMENTO DE OFICINA ===");
        
        // Cadastro inicial de dados para demonstração
        inicializarDadosDemonstracao();
        
        boolean executando = true;
        while (executando) {
            exibirMenuPrincipal();
            int opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    menuClientes();
                    break;
                case 2:
                    menuVeiculos();
                    break;
                case 3:
                    menuFuncionarios();
                    break;
                case 4:
                    menuServicos();
                    break;
                case 5:
                    menuAgendamentos();
                    break;
                case 6:
                    menuFinanceiro();
                    break;
                case 7:
                    menuRelatorios();
                    break;
                case 8:
                    menuEstoque();
                    break;    
                case 9:
                    salvarDados();
                    break;
                case 0:
                    executando = false;
                    System.out.println("Encerrando o sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        scanner.close();
    }
    private static void carregarDados() {
        File arquivo = new File(ARQUIVO_DADOS);
    
        if (!arquivo.exists()) {
            System.out.println("Arquivo não encontrado. Criando novo sistema...");
            oficina = new Oficina();
            inicializarDadosDemonstracao();
            salvarDados(); // Cria o arquivo automaticamente
        } else {
            try {
                oficina = JsonUtil.carregarOficina(ARQUIVO_DADOS);
                System.out.println("Dados carregados com sucesso!");
            } catch (IOException e) {
                System.err.println("Erro ao carregar dados. Criando novo sistema...");
                oficina = new Oficina();
                inicializarDadosDemonstracao();
        }
    }
}

    public static void salvarDados() {
        try {
            JsonUtil.salvarParaJson(oficina, ARQUIVO_DADOS);
            System.out.println("Dados salvos com sucesso no arquivo: " + ARQUIVO_DADOS);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }
    
    private static void exibirMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Clientes");
        System.out.println("2. Veículos");
        System.out.println("3. Funcionários");
        System.out.println("4. Serviços");
        System.out.println("5. Agendamentos");
        System.out.println("6. Financeiro");
        System.out.println("7. Relatórios");
        System.out.println("8. Estoque");  // Novo menu
        System.out.println("9. Salvar alteracoes");
        System.out.println("0. Sair");
    }
    private static void menuEstoque() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU ESTOQUE ===");
            System.out.println("1. Adicionar item ao estoque");
            System.out.println("2. Remover item do estoque");
            System.out.println("3. Atualizar quantidade");
            System.out.println("4. Listar estoque");
            System.out.println("0. Voltar");
            
            int opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    adicionarItemEstoque();
                    break;
                case 2:
                    removerItemEstoque();
                    break;
                case 3:
                    atualizarItemEstoque();
                    break;
                case 4:
                    oficina.listarEstoque();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    

    private static void menuClientes() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU CLIENTES ===");
            System.out.println("1. Cadastrar novo cliente");
            System.out.println("2. Listar todos os clientes");
            System.out.println("3. Buscar cliente por nome");
            System.out.println("4. Remover cliente");
            System.out.println("5. Adicionar veículo a cliente");
            System.out.println("0. Voltar");
            
            int opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    cadastrarCliente();
                    break;
                case 2:
                    oficina.listarClientes();
                    break;
                case 3:
                    buscarClientePorNome();
                    break;
                case 4:
                    removerCliente();
                    break;
                case 5:
                    adicionarVeiculoACliente();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuVeiculos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU VEÍCULOS ===");
            System.out.println("1. Listar todos os veículos");
            System.out.println("2. Buscar veículo por placa");
            System.out.println("0. Voltar");
            
            int opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    listarTodosVeiculos();
                    break;
                case 2:
                    buscarVeiculoPorPlaca();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuFuncionarios() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU FUNCIONÁRIOS ===");
            System.out.println("1. Cadastrar novo funcionário");
            System.out.println("2. Listar todos os funcionários");
            System.out.println("3. Buscar funcionário por nome");
            System.out.println("4. Remover funcionário");
            System.out.println("0. Voltar");
            
            int opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    cadastrarFuncionario();
                    break;
                case 2:
                    oficina.listarFuncionarios();
                    break;
                case 3:
                    buscarFuncionarioPorNome();
                    break;
                case 4:
                    removerFuncionario();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuServicos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU SERVIÇOS ===");
            System.out.println("1. Cadastrar novo serviço");
            System.out.println("2. Listar todos os serviços");
            System.out.println("3. Remover serviço");
            System.out.println("0. Voltar");
            
            int opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    cadastrarServico();
                    break;
                case 2:
                    oficina.listarServicos();
                    break;
                case 3:
                    removerServico();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuAgendamentos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU AGENDAMENTOS ===");
            System.out.println("1. Realizar novo agendamento");
            System.out.println("2. Listar todos os agendamentos");
            System.out.println("3. Listar agendamentos por cliente");
            System.out.println("4. Listar agendamentos por status");
            System.out.println("5. Cancelar agendamento");
            System.out.println("6. Concluir agendamento");
            System.out.println("0. Voltar");
            
            int opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    realizarAgendamento();
                    break;
                case 2:
                    oficina.getAgenda().listarAgendamentos();
                    break;
                case 3:
                    listarAgendamentosPorCliente();
                    break;
                case 4:
                    listarAgendamentosPorStatus();
                    break;
                case 5:
                    cancelarAgendamento();
                    break;
                case 6:
                    concluirAgendamento();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuFinanceiro() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU FINANCEIRO ===");
            System.out.println("1. Registrar pagamento");
            System.out.println("2. Registrar despesa");
            System.out.println("0. Voltar");
            
            int opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    registrarPagamento();
                    break;
                case 2:
                    registrarDespesa();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuRelatorios() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU RELATÓRIOS ===");
            System.out.println("1. Relatório diário");
            System.out.println("2. Relatório mensal");
            System.out.println("3. Relatório de clientes");
            System.out.println("4. Relatório de funcionários");
            System.out.println("0. Voltar");
            
            int opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    gerarRelatorioDiario();
                    break;
                case 2:
                    gerarRelatorioMensal();
                    break;
                case 3:
                    oficina.listarClientes();
                    break;
                case 4:
                    oficina.listarFuncionarios();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    // Métodos auxiliares para operações específicas
    private static void adicionarItemEstoque() {
        System.out.println("\n=== ADICIONAR ITEM ===");
        String codigo = lerString("Código: ");
        String nome = lerString("Nome: ");
        int quantidade = lerInteiro("Quantidade: ");
        
        oficina.adicionarItemEstoque(codigo, nome, quantidade);
    }
    
    private static void removerItemEstoque() {
        String codigo = lerString("Digite o código do item a remover: ");
        oficina.removerItemEstoque(codigo);
        System.out.println("Item removido do estoque!");
    }
    
    private static void atualizarItemEstoque() {
        String codigo = lerString("Digite o código do item: ");
        int quantidade = lerInteiro("Nova quantidade: ");
        oficina.atualizarItemEstoque(codigo, quantidade);
        System.out.println("Quantidade atualizada!");
    }
    
    
    private static void cadastrarCliente() {
        System.out.println("\n=== CADASTRAR CLIENTE ===");
        String nome = lerString("Nome: ");
        String telefone = lerString("Telefone: ");
        String endereco = lerString("Endereço: ");
        
        Cliente novoCliente = new Cliente(nome, telefone, endereco);
        oficina.cadastrarCliente(novoCliente);
    }

    private static void buscarClientePorNome() {
        String nome = lerString("Digite o nome do cliente: ");
        oficina.getClientes().stream()
            .filter(c -> c.getNome().equalsIgnoreCase(nome))
            .forEach(System.out::println);
    }

    private static void removerCliente() {
        String telefone = lerString("Digite o telefone do cliente a ser removido: ");
        oficina.removerCliente(telefone);
    }

    private static void adicionarVeiculoACliente() {
        String telefone = lerString("Digite o telefone do cliente: ");
        Cliente cliente = oficina.getClientes().stream()
            .filter(c -> c.getTelefone().equals(telefone))
            .findFirst()
            .orElse(null);
        
        if (cliente != null) {
            System.out.println("\n=== CADASTRAR VEÍCULO ===");
            String modelo = lerString("Modelo: ");
            String placa = lerString("Placa: ");
            int ano = lerInteiro("Ano: ");
            String marca = lerString("Marca: ");
            String cor = lerString("Cor: ");
            
            Veiculo novoVeiculo = new Veiculo(modelo, placa, ano, marca, cor);
            cliente.adicionarVeiculo(novoVeiculo);
        } else {
            System.out.println("Cliente não encontrado!");
        }
    }

    private static void listarTodosVeiculos() {
        System.out.println("\n=== TODOS OS VEÍCULOS ===");
        oficina.getClientes().forEach(cliente -> {
            System.out.println("\nCliente: " + cliente.getNome());
            cliente.listarVeiculos();
        });
    }

    private static void buscarVeiculoPorPlaca() {
        String placa = lerString("Digite a placa do veículo: ");
        oficina.getClientes().stream()
            .flatMap(cliente -> cliente.getVeiculos().stream())
            .filter(v -> ((Veiculo)v).getPlaca().equalsIgnoreCase(placa))
            .forEach(System.out::println);
    }

    private static void cadastrarFuncionario() {
        System.out.println("\n=== CADASTRAR FUNCIONÁRIO ===");
        String nome = lerString("Nome: ");
        String telefone = lerString("Telefone: ");
        String endereco = lerString("Endereço: ");
        String cargo = lerString("Cargo: ");
        double salario = lerDouble("Salário: ");
        String matricula = lerString("Matrícula: ");
        
        Funcionario novoFuncionario;
        if (cargo.equalsIgnoreCase("Administrador")) {
            novoFuncionario = new Administrador(nome, telefone, endereco, salario, matricula);
        } else {
            novoFuncionario = new Funcionario(nome, telefone, endereco, cargo, salario, matricula);
        }
        
        oficina.contratarFuncionario(novoFuncionario);
    }

    private static void buscarFuncionarioPorNome() {
        String nome = lerString("Digite o nome do funcionário: ");
        oficina.getFuncionarios().stream()
            .filter(f -> f.getNome().equalsIgnoreCase(nome))
            .forEach(System.out::println);
    }

    private static void removerFuncionario() {
        String matricula = lerString("Digite a matrícula do funcionário a ser removido: ");
        oficina.demitirFuncionario(matricula);
    }

    private static void cadastrarServico() {
        System.out.println("\n=== CADASTRAR SERVIÇO ===");
        String nome = lerString("Nome: ");
        String descricao = lerString("Descrição: ");
        double valor = lerDouble("Valor: ");
        int tempo = lerInteiro("Tempo estimado (minutos): ");
        
        Servico novoServico = new Servico(nome, descricao, valor, tempo);
        oficina.adicionarServico(novoServico);
    }

    private static void removerServico() {
        String nome = lerString("Digite o nome do serviço a ser removido: ");
        oficina.removerServico(nome);
    }

    private static void realizarAgendamento() {
        System.out.println("\n=== REALIZAR AGENDAMENTO ===");
        
        // Listar clientes para seleção
        System.out.println("\nClientes disponíveis:");
        oficina.listarClientes();
        String telefoneCliente = lerString("Digite o telefone do cliente: ");
        Cliente cliente = oficina.getClientes().stream()
            .filter(c -> c.getTelefone().equals(telefoneCliente))
            .findFirst()
            .orElse(null);
        
        if (cliente == null) {
            System.out.println("Cliente não encontrado!");
            return;
        }
        
        // Listar veículos do cliente
        System.out.println("\nVeículos do cliente:");
        cliente.listarVeiculos();
        String placa = lerString("Digite a placa do veículo: ");
        Veiculo veiculo = cliente.getVeiculos().stream()
            .filter(v -> v.getPlaca().equalsIgnoreCase(placa))
            .findFirst()
            .orElse(null);
        
        if (veiculo == null) {
            System.out.println("Veículo não encontrado!");
            return;
        }
        
        // Listar serviços disponíveis
        System.out.println("\nServiços disponíveis:");
        oficina.listarServicos();
        String nomeServico = lerString("Digite o nome do serviço: ");
        
        // Listar funcionários disponíveis
        System.out.println("\nFuncionários disponíveis:");
        oficina.listarFuncionarios();
        String matriculaFuncionario = lerString("Digite a matrícula do funcionário: ");
        Funcionario funcionario = oficina.getFuncionarios().stream()
            .filter(f -> f.getMatricula().equalsIgnoreCase(matriculaFuncionario))
            .findFirst()
            .orElse(null);
        
        if (funcionario == null) {
            System.out.println("Funcionário não encontrado!");
            return;
        }
        
        String dataHora = lerString("Digite a data e hora do agendamento (dd/MM/yyyy HH:mm): ");
        String status = "A fazer";
        oficina.agendarServico(cliente, veiculo, nomeServico, funcionario, dataHora, status);
    }

    private static void listarAgendamentosPorCliente() {
        String nome = lerString("Digite o nome do cliente: ");
        oficina.getAgenda().listarAgendamentosPorCliente(nome);
    }

    private static void listarAgendamentosPorStatus() {
        String status = lerString("Digite o status (Agendado/Concluído/Cancelado): ");
        oficina.getAgenda().listarAgendamentosPorStatus(status);
    }

    private static void cancelarAgendamento() {
        oficina.getAgenda().listarAgendamentos();
        int index = lerInteiro("Digite o número do agendamento a cancelar: ");
        oficina.getAgenda().cancelarAgendamento(index);
    }

    private static void concluirAgendamento() {
        oficina.getAgenda().listarAgendamentos();
        int index = lerInteiro("Digite o número do agendamento a concluir: ");
        oficina.getAgenda().concluirAgendamento(index);
        
        // Registrar pagamento automaticamente ao concluir
        Agenda.Agendamento ag = oficina.getAgenda().getAgendamentos().get(index);
        oficina.registrarPagamento(ag.getServico().getValor(), 
                                 "Serviço: " + ag.getServico().getNome() + " - Cliente: " + ag.getCliente().getNome(), 
                                 ag.getDataHora().split(" ")[0]);
    }

    private static void registrarPagamento() {
        double valor = lerDouble("Valor: ");
        String descricao = lerString("Descrição: ");
        String data = lerString("Data (dd/MM/yyyy): ");
        oficina.registrarPagamento(valor, descricao, data);
    }

    private static void registrarDespesa() {
        double valor = lerDouble("Valor: ");
        String descricao = lerString("Descrição: ");
        String data = lerString("Data (dd/MM/yyyy): ");
        oficina.registrarDespesa(valor, descricao, data);
    }

    private static void gerarRelatorioDiario() {
        String data = lerString("Digite a data (dd/MM/yyyy): ");
        oficina.gerarRelatorioDiario(data);
    }

    private static void gerarRelatorioMensal() {
        int mes = lerInteiro("Digite o mês (1-12): ");
        int ano = lerInteiro("Digite o ano: ");
        oficina.gerarRelatorioMensal(mes, ano);
    }

    // Métodos auxiliares para leitura de entrada
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
                System.out.println("Por favor, digite um número válido!");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um valor válido!");
            }
        }
    }

    // Método para inicializar dados de demonstração
    private static void inicializarDadosDemonstracao() {
        // Cadastrar funcionários de demonstração
        Funcionario mecanico = new Funcionario("João Silva", "11987654321", "Rua A, 123", 
                                            "Mecânico", 2500.00, "MEC001");
        Administrador admin = new Administrador("Maria Souza", "11912345678", "Rua B, 456", 
                                             4500.00, "ADM001");
        oficina.contratarFuncionario(mecanico);
        oficina.contratarFuncionario(admin);
        
        // Cadastrar clientes de demonstração
        Cliente cliente1 = new Cliente("Carlos Oliveira", "11955556666", "Rua C, 789");
        Veiculo veiculo1 = new Veiculo("Gol", "ABC1234", 2018, "Volkswagen", "Prata");
        cliente1.adicionarVeiculo(veiculo1);
        
        Cliente cliente2 = new Cliente("Ana Santos", "11944445555", "Rua D, 1011");
        Veiculo veiculo2 = new Veiculo("Civic", "XYZ5678", 2020, "Honda", "Preto");
        cliente2.adicionarVeiculo(veiculo2);
        
        oficina.cadastrarCliente(cliente1);
        oficina.cadastrarCliente(cliente2);
        oficina.adicionarItemEstoque("P001", "Pastilha de Freio", 20);
        oficina.adicionarItemEstoque("F001", "Filtro de Óleo", 15);
        oficina.adicionarItemEstoque("V001", "Vela de Ignição", 30);
    
    

        // Adicionar serviços adicionais
        oficina.adicionarServico(new Servico("Troca de Pastilhas", "Troca das pastilhas de freio", 180.00, 45));
        oficina.adicionarServico(new Servico("Diagnóstico Eletrônico", "Leitura de falhas do sistema", 150.00, 30));
        
        System.out.println("\nDados de demonstração cadastrados com sucesso!");
    }
}