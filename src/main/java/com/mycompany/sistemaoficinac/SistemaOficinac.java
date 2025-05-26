

package com.mycompany.sistemaoficinac;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


/**
 * Classe principal do sistema de gerenciamento de oficina mecânica.
 * <p>
 * Controla todo o fluxo do programa, incluindo menus, operações e persistência de dados.
 * </p>
 * 
 * @version 1.0
 * @author Gabriel
 */
public class SistemaOficinac {
    private static final String ARQUIVO_DADOS = "data/ficina.json";
    private static Oficina oficina = new Oficina();
    private static Scanner scanner = new Scanner(System.in);
    private static Login loginManager = new Login(); // Inicializa com um objeto padrão
    protected static int contadorVeiculos = 0; // Contador de veículos (protected)
/**
 * Ponto de entrada principal do sistema de oficina.
 * <p>
 * Este método inicia a aplicação, carrega os dados existentes e controla
 * o loop principal do menu interativo.
 * </p>
 * 
 * @param args Argumentos da linha de comando (não utilizados neste sistema)
 * @throws IOException Se ocorrer um erro durante o carregamento dos dados
 * @see #carregarDados()
 * @see #inicializarDadosDemonstracao()
 */
    
    public static void main(String[] args) {
        
        try {
            oficina.carregarDados();    // Carrega os dados da oficina e do login
            
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            System.err.println("Erro ao carregar os dados: " + e.getMessage());
            System.out.println("Deseja criar um novo sistema? (S/N)");
            String resposta = scanner.nextLine().trim().toUpperCase();
            if ("S".equals(resposta)) {
                System.out.println("Criando novo sistema...");
                oficina = new Oficina();
                loginManager = new Login();
                //inicializarDadosDemonstracao();
            } else {
                System.out.println("Operação cancelada. O sistema será encerrado.");
                System.exit(0);
            }
        }
        System.out.println("=== SISTEMA DE GERENCIAMENTO DE OFICINA ===");
    
        // Solicita credenciais de login
        System.out.println("=== SISTEMA DE LOGIN ===");
        System.out.println("Login padrao: admin/admin123  ou  func/func123");
        boolean log = false;
        String tipoUsuario = null;
        while(!log){
            System.out.print("Usuário: ");
            String usuario = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();  
            
            
            tipoUsuario = loginManager.login(usuario, senha);
        
            if (tipoUsuario == null) {
                System.out.println("Login falhou! Usuário ou senha inválidos.");
            }else{
                System.out.println("Login bem-sucedido! Tipo de usuário: " + tipoUsuario);
                log = true;
            }
        }
        
    
  
            
        if (tipoUsuario.equals("Administrador")) {
            boolean voltar = false;
            while (!voltar) {
                exibirMenuAdministrador();
                int opcao = Oficina.lerInteiro("Digite sua opção: ");
                switch (opcao) {
                    case 1:
                        menuFuncionarios();
                        break;
                    case 2:
                        menuFinanceiro();
                        break;
                    case 3:
                        menuRelatorios();
                        break;
                    case 4:
                        oficina.alterarSenha(); 
                        break;
                    case 0:
                        oficina.ordenarTudo(oficina.getClientes(), oficina.getAgenda().getAgendamentos() , oficina.getServicos());
                        try {
                            oficina.salvarDados();
                        } catch (IOException e) {
                            System.err.println("Erro ao salvar os dados: " + e.getMessage());
                            System.out.println("Os dados não foram salvos. Verifique o problema e tente novamente.");
                        }
                        voltar = true;
                        break;
                }
            }
        } else if (tipoUsuario.equals("Funcionario")) {
            boolean voltar = false;
            while (!voltar) {
                exibirMenuFuncionario();
                int opcao = Oficina.lerInteiro("Digite sua opção: ");
                switch (opcao) {
                    case 1:
                        menuAgendamentos();
                        break;
                    case 2:
                        menuVeiculos();
                        break;
                    case 3:
                        menuEstoque();
                        break;
                    case 4:
                        menuClientes();
                        break;
                    case 5:
                        menuServicos();
                    case 6: 
                        menuElevadores();
                        break;
                    case 7:
                        menuPonto();
                        break;
                    case 0:
                        try {
                            oficina.salvarDados();
                        } catch (IOException e) {
                            System.err.println("Erro ao salvar os dados: " + e.getMessage());
                            System.out.println("Os dados não foram salvos. Verifique o problema e tente novamente.");
                        }
                        voltar = true;
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            }
        
        } else {
            System.out.println("Tipo de usuário inválido!");
        }
            
            
            
        
    }



    /** 
     * Exibe o menu de opções para o administrador.
     */
    private static void exibirMenuAdministrador() {
        System.out.println("\n=== MENU ADMINISTRADOR ===");
        System.out.println("1. Gerenciar Funcionários");
        System.out.println("2. Gerenciar Financeiro");  
        System.out.println("3. Gerar Relatórios");
        System.out.println("4. Alterar senha");
        System.out.println("0. Salvar e sair");
    }
    /**
     * Exibe o menu de opções para o funcionário.
     */
    private static void exibirMenuFuncionario() {
        System.out.println("\n=== MENU FUNCIONÁRIO ===");
        System.out.println("1. Gerenciar Agendamentos");    
        System.out.println("2. Gerenciar Veículos");
        System.out.println("3. Gerenciar Estoque");
        System.out.println("4. Gerenciar Clientes");
        System.out.println("5. Gerenciar Serviços");
        System.out.println("6. Gerenciar Elevadores");
        System.out.println("7. Gerenciar Ponto");
        System.out.println("0. Sair");
        
    }
    /**
     * Exibe o menu de opções para o estoque.
     */
    private static void menuEstoque() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU ESTOQUE ===");
            System.out.println("1. Adicionar item ao estoque");
            System.out.println("2. Remover item do estoque");
            System.out.println("3. Atualizar quantidade");
            System.out.println("4. Listar estoque");
            System.out.println("5. Vender item");
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    oficina.adicionarItemEstoque();
                    break;
                case 2:
                    oficina.removerItemEstoque();
                    break;
                case 3:
                    oficina.atualizarItemEstoque();
                    break;
                case 4:
                    oficina.listarEstoque();
                    break;
                case 5:
                    oficina.venderItemEstoque();
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    /***
     * Registra o ponto do funcionário.
     * @param entrada true se for registro de entrada, false se for registro de saída
     * @see PontoFuncionario
     */
    private static void menuPonto() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== CONTROLE DE PONTO ===");
            System.out.println("1. Registrar entrada");
            System.out.println("2. Registrar saída");
            System.out.println("3. Listar registros");
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            switch (opcao) {
                case 1:
                    oficina.registrarPonto(true);
                    break;
                case 2:
                    oficina.registrarPonto(false);
                    break;
                case 3:
                    oficina.listarRegistrosPonto();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }


    /***
     * Exibe o menu de opções para gerenciar elevadores.
     */
    private static void menuElevadores() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU DE ELEVADORES ===");
            System.out.println("1. Listar elevadores");
            System.out.println("2. Alocar elevador");
            System.out.println("3. Liberar elevador");
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            switch (opcao) {
                case 1:
                    Elevador.listarElevadores();
                    break;
                case 2:
                    alocarElevador();
                    break;
                case 3:
                    oficina.liberarElevador();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    /**
     * Exibe o menu de opções para gerenciar clientes.
     */

    private static void menuClientes() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU CLIENTES ===");
            System.out.println("1. Cadastrar novo cliente");
            System.out.println("2. Listar todos os clientes");
            System.out.println("3. Buscar cliente por nome");
            System.out.println("4. Remover cliente");
            System.out.println("5. Adicionar veículo a cliente");
            System.out.println("6. Alterar dados do cliente");
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    oficina.cadastrarCliente();
                    break;
                case 2:
                    oficina.listarClientes();
                    break;
                case 3:
                    oficina.buscarClientePorNome();
                    break;
                case 4:
                    oficina.removerCliente();
                    break;
                case 5:
                    oficina.adicionarVeiculoACliente();
                    break;
                case 6:
                    oficina.alterarDadosCliente();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    /**
     * Exibe o menu de opções para gerenciar veículos.
     */
    private static void menuVeiculos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU VEÍCULOS ===");
            System.out.println("1. Listar todos os veículos");
            System.out.println("2. Buscar veículo por placa");
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    oficina.listarTodosVeiculos();
                    break;
                case 2:
                    oficina.buscarVeiculoPorPlaca();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    /**
     * Exibe o menu de opções para gerenciar funcionários.
     */
    private static void menuFuncionarios() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU FUNCIONÁRIOS ===");
            System.out.println("1. Contratar novo funcionário");
            System.out.println("2. Listar todos os funcionários");
            System.out.println("3. Buscar funcionário por nome");
            System.out.println("4. Demitir funcionário");
            System.out.println("5. Alterar dados do funcionário");
            System.out.println("0. Voltar");

            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    oficina.contratarFuncionario();
                    break;
                case 2:
                    oficina.listarFuncionarios();
                    break;
                case 3:
                   oficina.buscarFuncionarioPorNome();
                    break;
                case 4:
                    oficina.demitirFuncionario();
                    break;
                case 5:
                   oficina.alterarDadosFuncionario();
                    break;   
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    /**
     * Exibe o menu de opções para gerenciar serviços.
     */
    private static void menuServicos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU SERVIÇOS ===");
            System.out.println("1. Cadastrar novo serviço");
            System.out.println("2. Listar todos os serviços");
            System.out.println("3. Remover serviço");
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    oficina.adicionarServico();
                    break;
                case 2:
                    oficina.listarServicos();
                    break;
                case 3:
                    oficina.removerServico();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    /**
     * Exibe o menu de opções para gerenciar agendamentos.
     */
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
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    oficina.agendarServico();
                    break;
                case 2:
                    oficina.getAgenda().listarAgendamentos();
                    break;
                case 3:
                    oficina.listarAgendamentosPorCliente();
                    break;
                case 4:
                    oficina.listarAgendamentosPorStatus();
                    break;
                case 5:
                   oficina.cancelarAgendamento();
                    break;
                case 6:
                   oficina.concluirAgendamento();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
    /**
     * Exibe o menu de opções para gerenciar financeiro.
     */
    private static void menuFinanceiro() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU FINANCEIRO ===");
            System.out.println("1. Registrar pagamento de cliente");
            System.out.println("2. Registrar despesa geral");
            System.out.println("3. Registrar compra de peças");
            System.out.println("4. Registrar pagamento de salários");
            System.out.println("5. Listar transações"); // Nova opção
            System.out.println("6. Gerar relatório mensal");
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    oficina.registrarPagamento();
                    break;
                case 2:
                    oficina.registrarDespesa();
                    break;
                case 3:
                    oficina.registrarCompraPecas();
                    break;
                case 4:
                    oficina.registrarPagamentoSalarios();
                    break;
                case 5:
                    menuListarTransacoes(); // Novo submenu
                    break;
                case 6:
                    menuRelatorios();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuListarTransacoes() {
    boolean voltar = false;
    while (!voltar) {
        System.out.println("\n=== LISTAR TRANSAÇÕES ===");
        System.out.println("1. Listar todas as transações");
        System.out.println("2. Filtrar por período");
        System.out.println("3. Filtrar por categoria");
        System.out.println("4. Filtrar por tipo (Entrada/Saída)");
        System.out.println("5. Filtrar por responsável");
        System.out.println("0. Voltar");
        
        int opcao = Oficina.lerInteiro("Digite sua opção: ");
        
        switch (opcao) {
            case 1:
                oficina.listarTodasTransacoes();
                break;
            case 2:
                oficina.listarTransacoesPorPeriodo();
                break;
            case 3:
                oficina.listarTransacoesPorCategoria();
                break;
            case 4:
                oficina.listarTransacoesPorTipo();
                break;
            case 5:
               oficina.listarTransacoesPorResponsavel();
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
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    oficina.gerarRelatorioDiario();
                    break;
                case 2:
                    oficina.gerarRelatorioMensal();
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
    /***
     * Aloca o elevador
     */
    private static void alocarElevador() {
        System.out.println("\n=== ALOCAR ELEVADOR ===");
        Elevador.listarElevadores();
        int numero = Oficina.lerInteiro("Digite o número do elevador a ser alocado (1-3): ");
        
        if (numero < 1 || numero > 3) {
            System.out.println("Número inválido!");
            return;
        }
        
        Elevador elevador = Elevador.alocarElevador(numero - 1); // Índice base 0
        if (elevador != null) {
            String modeloVeiculo = Oficina.lerString("Digite o modelo do veículo para o elevador: ");
            elevador.setModelo(modeloVeiculo);
            System.out.println("Elevador " + numero + " alocado para o veículo: " + modeloVeiculo);
        }
    }

    /*private static void inicializarDadosDemonstracao() {
        // Cadastrar funcionários de demonstração
        Funcionario mecanico = new Funcionario("João Silva", "11987654321", "Rua A, 123", 
                                            "Mecânico", 2500.00, "MEC001");
        Administrador admin = new Administrador("Maria Souza", "11912345678", "Rua B, 456", 
                                            4500.00, "ADM001");
        oficina.contratarFuncionario(mecanico);
        oficina.contratarFuncionario(admin);
        
        // Cadastrar clientes de demonstração
        Cliente cliente1 = new Cliente("Carlos Oliveira", "11955556666", "Rua C, 789",null, "12345678909");
        Veiculo veiculo1 = new Veiculo("Gol", "ABC1234", 2018, "Volkswagen", "Prata");
        cliente1.adicionarVeiculo(veiculo1);
        
        Cliente cliente2 = new Cliente("Ana Santos", "11944445555", "Rua D, 1011",null, "98765432100");
        Veiculo veiculo2 = new Veiculo("Civic", "XYZ5678", 2020, "Honda", "Preto");
        cliente2.adicionarVeiculo(veiculo2);
        
        oficina.cadastrarCliente(cliente1);
        oficina.cadastrarCliente(cliente2);
        
        // Adicionar itens ao estoque
        oficina.adicionarItemEstoque("P001", "Pastilha de Freio", 20);
        oficina.adicionarItemEstoque("F001", "Filtro de Óleo", 15);
        oficina.adicionarItemEstoque("V001", "Vela de Ignição", 30);

        // Adicionar serviços adicionais
        oficina.adicionarServico(new Servico("Troca de Pastilhas", "Troca das pastilhas de freio", 180.00, 45));
        oficina.adicionarServico(new Servico("Diagnóstico Eletrônico", "Leitura de falhas do sistema", 150.00, 30));
        oficina.adicionarServico(new Servico("Troca de Óleo", "Troca do óleo do motor", 200.00, 60));
        oficina.adicionarServico(new Servico("Alinhamento", "Alinhamento de direção", 100.00, 30));
        oficina.adicionarServico(new Servico("Balanceamento", "Balanceamento de rodas", 80.00, 30));
        oficina.adicionarServico(new Servico("Troca de Filtro de Ar", "Substituição do filtro de ar", 50.00, 20));
        oficina.adicionarServico(new Servico("Troca de Filtro de Combustível", "Substituição do filtro de combustível", 70.00, 25));
        oficina.adicionarServico(new Servico("Troca de Vela de Ignição", "Substituição das velas de ignição", 60.00, 30));
        oficina.adicionarServico(new Servico("Troca de Bateria", "Substituição da bateria do veículo", 250.00, 60));
        


        // Agendar serviços para os clientes
        //oficina.agendarServico(cliente1, veiculo1, null, mecanico, "10/05/2025 14:00", "Agendado");
        //oficina.agendarServico(cliente2, veiculo2, null, mecanico, "11/05/2025 09:00", "Agendado");



        System.out.println("\nDados de demonstração cadastrados com sucesso!");
    }
    */
    
}