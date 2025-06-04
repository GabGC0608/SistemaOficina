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
    /** Caminho do arquivo de dados principal do sistema */
    private static final String ARQUIVO_DADOS = "data/ficina.json";
    
    /** Instância principal da oficina que gerencia todas as operações */
    private static Oficina oficina = new Oficina();
    
    /** Scanner para leitura de dados do console */
    private static Scanner scanner = new Scanner(System.in);
    
    /** Gerenciador de login e autenticação do sistema */
    private static Login loginManager = new Login(); // Inicializa com um objeto padrão
    
    /** Contador do total de veículos cadastrados no sistema */
    private static int contadorVeiculosPrivate = 0;
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
            //inicializarDadosDemonstracao();
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            System.err.println("Erro ao carregar os dados: " + e.getMessage());
            System.out.println("Deseja criar um novo sistema? (S/N)");
            String resposta = scanner.nextLine().trim().toUpperCase();
            if ("S".equals(resposta)) {
                System.out.println("Criando novo sistema...");
                oficina = new Oficina();
                loginManager = new Login();
                inicializarDadosDemonstracao();
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
                        break;
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
     * Exibe o menu principal do administrador.
     * Apresenta opções para gerenciar funcionários, financeiro, relatórios e alterar senha.
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
     * Exibe o menu principal do funcionário.
     * Apresenta opções para gerenciar agendamentos, veículos, estoque, clientes, serviços, elevadores e ponto.
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
     * Exibe o menu de gerenciamento de estoque.
     * Permite adicionar, remover, atualizar e listar itens do estoque, além de realizar vendas de peças.
     */
    private static void menuEstoque() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU ESTOQUE ===");
            System.out.println("1. Adicionar item ao estoque");
            System.out.println("2. Remover item do estoque");
            System.out.println("3. Atualizar item do estoque");
            System.out.println("4. Listar itens do estoque");
            System.out.println("5. Vender peças");
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
                    oficina.venderPecas();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }
    /**
     * Exibe o menu de gerenciamento de ponto.
     * Permite registrar entrada e saída de funcionários e listar os registros de ponto.
     */
    private static void menuPonto() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU PONTO ===");
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
                    oficina.mostrarRegistroPonto();
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
     * Exibe o menu de gerenciamento de elevadores.
     * Permite listar, alocar e liberar elevadores da oficina.
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
     * Exibe o menu de gerenciamento de clientes.
     * Permite cadastrar, listar, buscar e remover clientes, além de adicionar veículos e alterar dados.
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
                    incrementarContadorVeiculosPrivate();
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
     * Exibe o menu de gerenciamento de veículos.
     * Permite adicionar, listar e buscar veículos, além de mostrar o total de veículos cadastrados.
     */
    private static void menuVeiculos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU VEÍCULOS ===");
            System.out.println("1. Adicionar veículo a cliente");
            System.out.println("2. Listar todos os veículos");
            System.out.println("3. Buscar veículo por placa");
            System.out.println("4. Mostrar total de veículos");
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    oficina.adicionarVeiculoACliente();
                    break;
                case 2:
                    oficina.listarVeiculos();
                    break;
                case 3:
                    String placa = Oficina.lerString("Digite a placa do veículo: ");
                    Veiculo veiculo = oficina.buscarVeiculoPorPlaca(placa);
                    if (veiculo != null) {
                        System.out.println(veiculo);
                    } else {
                        System.out.println("Veículo não encontrado!");
                    }
                    break;
                case 4:
                    System.out.println("\n=== TOTAL DE VEÍCULOS ===");
                    System.out.println("Contador privado: " + getContadorVeiculosPrivate());
                    //System.out.println("Contador protegido: " + Sistema.contadorVeiculosProtegido);
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
     * Exibe o menu de gerenciamento de funcionários.
     * Permite contratar, demitir, listar e buscar funcionários, além de alterar seus dados.
     */
    private static void menuFuncionarios() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU FUNCIONÁRIOS ===");
            System.out.println("1. Contratar funcionário");
            System.out.println("2. Demitir funcionário");
            System.out.println("3. Listar funcionários");
            System.out.println("4. Buscar funcionário por nome");
            System.out.println("5. Alterar dados de funcionário");
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    oficina.contratarFuncionario();
                    break;
                case 2:
                    oficina.demitirFuncionario();
                    break;
                case 3:
                    oficina.listarFuncionarios();
                    break;
                case 4:
                    String nome = Oficina.lerString("Digite o nome do funcionário: ");
                    Funcionario funcionario = oficina.buscarFuncionarioPorNome(nome);
                    if (funcionario != null) {
                        System.out.println(funcionario);
                    } else {
                        System.out.println("Funcionário não encontrado!");
                    }
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
     * Exibe o menu de gerenciamento de serviços.
     * Permite cadastrar, listar e remover serviços disponíveis na oficina.
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
     * Exibe o menu de gerenciamento de agendamentos.
     * Permite agendar, cancelar, concluir e listar agendamentos de serviços.
     */
    private static void menuAgendamentos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU AGENDAMENTOS ===");
            System.out.println("1. Agendar serviço");
            System.out.println("2. Cancelar agendamento");
            System.out.println("3. Concluir agendamento");
            System.out.println("4. Listar todos agendamentos");
            System.out.println("5. Listar agendamentos por cliente");
            System.out.println("6. Listar agendamentos por status");
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            switch (opcao) {
                case 1:
                    oficina.agendarServico();
                    break;
                case 2:
                    oficina.cancelarAgendamento();
                    break;
                case 3:
                    oficina.concluirAgendamento();
                    break;
                case 4:
                    oficina.listarAgendamentos();
                    break;
                case 5:
                    oficina.listarAgendamentosPorCliente();
                    break;
                case 6:
                    oficina.listarAgendamentosPorStatus();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }
    /**
     * Exibe o menu de gerenciamento financeiro.
     * Permite registrar pagamentos, despesas, compras e gerar relatórios financeiros.
     */
    private static void menuFinanceiro() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU FINANCEIRO ===");
            System.out.println("1. Registrar pagamento");
            System.out.println("2. Registrar despesa");
            System.out.println("3. Registrar compra de peças");
            System.out.println("4. Registrar pagamento de salários");
            System.out.println("5. Gerar relatório diário");
            System.out.println("6. Gerar relatório mensal");
            System.out.println("7. Listar ordens de serviço");
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
                    oficina.gerarRelatorioDiario();
                    break;
                case 6:
                    oficina.gerarRelatorioMensal();
                    break;
                case 7:
                    menuListarTransacoes();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }

    /**
     * Exibe o menu de listagem de transações.
     * Permite filtrar e visualizar transações por diferentes critérios.
     */
    private static void menuListarTransacoes() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== LISTAR ORDENS DE SERVIÇO ===");
            System.out.println("1. Listar todas");
            System.out.println("2. Filtrar por período");
            System.out.println("3. Filtrar por categoria");
            System.out.println("4. Filtrar por tipo");
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
                    break;
            }
        }
    }
    /**
     * Exibe o menu de relatórios.
     * Permite gerar diferentes tipos de relatórios do sistema.
     */
    private static void menuRelatorios() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU RELATÓRIOS ===");
            System.out.println("1. Relatório diário");
            System.out.println("2. Relatório mensal");
            System.out.println("3. Relatório de clientes");
            System.out.println("4. Relatório de funcionários");
            System.out.println("5. Relatório estatístico detalhado");
            System.out.println("6. Relatório de desempenho dos mecânicos");
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
                case 5:
                    oficina.gerarRelatorioEstatistico();
                    break;
                case 6:
                    oficina.gerarRelatorioDesempenhoMecanicos();
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
    /**
     * Aloca um elevador para um veículo específico.
     * Solicita o número do elevador (1-3) e o modelo do veículo.
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

    /**
     * Inicializa o sistema com dados de demonstração para testes.
     * Cria funcionários, clientes, veículos, serviços e agendamentos de exemplo.
     * @param oficina Instância da oficina a ser inicializada com dados de demonstração
     */
    public static void inicializarDadosDemonstracao() {
        
        // Cadastrar funcionários de demonstração com especialidades
        Funcionario mecanicoMotor = new Funcionario("João Silva", "11987654321", "Rua A, 123", 
                                            "Mecânico", 2500.00, "MEC001", "Motor");
        Funcionario mecanicoEletrica = new Funcionario("Pedro Santos", "11987654322", "Rua B, 234",
                                            "Mecânico", 2500.00, "MEC002", "Elétrica");
        Funcionario mecanicoSuspensao = new Funcionario("Carlos Oliveira", "11987654323", "Rua C, 345",
                                            "Mecânico", 2500.00, "MEC003", "Suspensão");
        Administrador admin = new Administrador("Maria Souza", "11912345678", "Rua D, 456", 
                                            4500.00, "ADM001");
        
        oficina.contratarFuncionario(mecanicoMotor);
        oficina.contratarFuncionario(mecanicoEletrica);
        oficina.contratarFuncionario(mecanicoSuspensao);
        oficina.contratarFuncionario(admin);
        
        // Criar clientes e veículos
        Cliente cliente1 = new Cliente(
            "Roberto Oliveira",
            "11955556666",
            "Rua E, 789",
            oficina.anonimizarCPF("12345678909")
        );
                
        Veiculo veiculo1 = new Veiculo("Gol", "ABC1234", 2018, "Volkswagen", "Prata");
        cliente1.adicionarVeiculo(veiculo1);
        incrementarContadorVeiculosPrivate();
        
        Cliente cliente2 = new Cliente(
            "Ana Santos",
            "11944445555",
            "Rua F, 1011",
            oficina.anonimizarCPF("98765432100")
        );
                
        Veiculo veiculo2 = new Veiculo("Civic", "XYZ5678", 2020, "Honda", "Preto");
        cliente2.adicionarVeiculo(veiculo2);
        incrementarContadorVeiculosPrivate();
        
        oficina.getClientes().add(cliente1);
        oficina.getClientes().add(cliente2);
        
        // Adicionar itens ao estoque
        oficina.getEstoque().adicionarItem("P001", "Pastilha de Freio", 20, 150.00, "Pastilha de freio de alta performance");
        oficina.getEstoque().adicionarItem("F001", "Filtro de Óleo", 15, 45.00, "Filtro de óleo para motor");
        oficina.getEstoque().adicionarItem("O001", "Óleo Motor 5W30", 50, 35.00, "Óleo sintético 5W30");
        oficina.getEstoque().adicionarItem("V001", "Vela de Ignição", 30, 25.00, "Vela de ignição de iridium");
        oficina.getEstoque().adicionarItem("B001", "Bateria 60Ah", 10, 450.00, "Bateria selada 60Ah");
        oficina.getEstoque().adicionarItem("C001", "Correia Dentada", 15, 180.00, "Correia dentada de alta durabilidade");
        oficina.getEstoque().adicionarItem("A001", "Amortecedor", 8, 350.00, "Amortecedor dianteiro");
        oficina.getEstoque().adicionarItem("R001", "Radiador", 5, 600.00, "Radiador de água completo");
        oficina.getEstoque().adicionarItem("FA001", "Filtro de Ar", 25, 45.00, "Filtro de ar do motor");
    
        // Adicionar serviços diretamente
        oficina.getServicos().add(new Servico("Troca de Pastilhas", "Troca das pastilhas de freio", 180.00, 60));
        oficina.getServicos().add(new Servico("Troca de Óleo", "Troca do óleo do motor", 120.00, 45));
        oficina.getServicos().add(new Servico("Alinhamento", "Alinhamento de direção", 100.00, 30));
        oficina.getServicos().add(new Servico("Balanceamento", "Balanceamento de rodas", 80.00, 30));
        oficina.getServicos().add(new Servico("Revisão Completa", "Revisão geral do veículo", 350.00, 120));
        oficina.getServicos().add(new Servico("Diagnóstico Elétrico", "Diagnóstico do sistema elétrico", 150.00, 60));
        oficina.getServicos().add(new Servico("Troca de Correia", "Troca da correia dentada", 280.00, 90));
        oficina.getServicos().add(new Servico("Limpeza de Bicos", "Limpeza dos bicos injetores", 200.00, 60));
    
        // Criar alguns agendamentos de demonstração
        List<Servico> servicosAgendamento1 = new ArrayList<>();
        servicosAgendamento1.add(oficina.getServicos().get(0)); // Troca de Pastilhas
        servicosAgendamento1.add(oficina.getServicos().get(1)); // Troca de Óleo
        
        Agenda.Agendamento agendamento1 = oficina.getAgenda().adicionarAgendamento(
            cliente1, veiculo1, servicosAgendamento1, mecanicoMotor,
            "10/05/2024 09:00", "Agendado", mecanicoSuspensao
        );
    
        List<Servico> servicosAgendamento2 = new ArrayList<>();
        servicosAgendamento2.add(oficina.getServicos().get(4)); // Revisão Completa
        
        Agenda.Agendamento agendamento2 = oficina.getAgenda().adicionarAgendamento(
            cliente2, veiculo2, servicosAgendamento2, mecanicoEletrica,
            "11/05/2024 14:00", "Agendado", mecanicoMotor
        );

        List<Servico> servicosAgendamento3 = new ArrayList<>();
        servicosAgendamento3.add(oficina.getServicos().get(5)); // Diagnóstico Elétrico
        
        Agenda.Agendamento agendamento3 = oficina.getAgenda().adicionarAgendamento(
            cliente1, veiculo1, servicosAgendamento3, mecanicoEletrica,
            "12/05/2024 10:00", "Agendado", mecanicoMotor
        );
    
        // Adicionar registros de ponto de demonstração
        // MEC001 - Dois dias de registros
        oficina.getRegistrosPonto().add(new PontoFuncionario("MEC001", 
            LocalDateTime.of(2024, 5, 8, 8, 0), 
            LocalDateTime.of(2024, 5, 8, 17, 0)));
        oficina.getRegistrosPonto().add(new PontoFuncionario("MEC001", 
            LocalDateTime.of(2024, 5, 9, 8, 15), 
            LocalDateTime.of(2024, 5, 9, 17, 30)));
            
        // MEC002 - Um dia de registro
        oficina.getRegistrosPonto().add(new PontoFuncionario("MEC002", 
            LocalDateTime.of(2024, 5, 8, 7, 45), 
            LocalDateTime.of(2024, 5, 8, 16, 45)));
            
        // MEC003 - Um dia de registro
        oficina.getRegistrosPonto().add(new PontoFuncionario("MEC003", 
            LocalDateTime.of(2024, 5, 9, 8, 30), 
            LocalDateTime.of(2024, 5, 9, 17, 30)));
    
        // Criar ordens de serviço de demonstração diretamente
        // OS001: Troca de óleo e revisão geral
        OrdemDeServicoBuilder builder = new OrdemServicoConcretaBuilder();
        OrdemServicoDirector director = new OrdemServicoDirector(builder);

    // OS001: Troca de óleo e revisão geral
    List<Estoque.ItemEstoque> itensOS1 = new ArrayList<>();
    itensOS1.add(oficina.getEstoque().getItens().get(1)); // Filtro de Óleo
    itensOS1.add(oficina.getEstoque().getItens().get(2)); // Óleo Motor
    
    director.construirOrdemCompleta(
        "Entrada",
        "Troca de óleo e revisão geral",
        "08/05/2024",
        "Serviços",
        mecanicoMotor.getNome(),
        cliente1.getNome(),
        agendamento1,
        itensOS1
    );
    OrdemServico os1 = builder.build();
    oficina.getOrdensServico().add(os1);

    // OS002: Reparo elétrico e troca de bateria
    List<Estoque.ItemEstoque> itensOS2 = new ArrayList<>();
    itensOS2.add(oficina.getEstoque().getItens().get(4)); // Bateria
    
    director.construirOrdemCompleta(
        "Entrada",
        "Reparo elétrico e troca de bateria",
        "09/05/2024",
        "Serviços",
        mecanicoEletrica.getNome(),
        cliente2.getNome(),
        agendamento2,
        itensOS2
    );
    OrdemServico os2 = builder.build();
    oficina.getOrdensServico().add(os2);

    // OS003: Manutenção de suspensão
    List<Estoque.ItemEstoque> itensOS3 = new ArrayList<>();
    itensOS3.add(oficina.getEstoque().getItens().get(6)); // Amortecedor
    
    director.construirOrdemCompleta(
        "Entrada",
        "Manutenção de suspensão e alinhamento",
        "09/05/2024",
        "Serviços",
        mecanicoSuspensao.getNome(),
        cliente1.getNome(),
        agendamento3,
        itensOS3
    );
    OrdemServico os3 = builder.build();
    oficina.getOrdensServico().add(os3);
    
        // Registrar algumas transações de demonstração
        oficina.getCaixa().registrarEntrada(150.00, "Venda de óleo", "07/05/2024", "Produtos", mecanicoMotor.getNome(), null);
        oficina.getCaixa().registrarEntrada(80.00, "Diagnóstico simples", "07/05/2024", "Serviços", mecanicoEletrica.getNome(), null);
        oficina.getCaixa().registrarSaida(1500.00, "Compra de ferramentas", "05/05/2024", "Equipamentos", admin.getNome(), null);
        oficina.getCaixa().registrarSaida(800.00, "Conta de energia", "06/05/2024", "Despesas Fixas", admin.getNome(), null);
        oficina.getCaixa().registrarSaida(2500.00, "Compra de peças diversas", "07/05/2024", "Estoque", admin.getNome(), null);
    
        System.out.println("\nDados de demonstração cadastrados com sucesso!");
        System.out.println("Funcionários cadastrados: " + oficina.getFuncionarios().size());
        System.out.println("Clientes cadastrados: " + oficina.getClientes().size());
        System.out.println("Itens em estoque: " + oficina.getEstoque().getItens().size());
        System.out.println("Serviços disponíveis: " + oficina.getServicos().size());
        System.out.println("Agendamentos realizados: " + oficina.getAgenda().getAgendamentos().size());
        System.out.println("Transações registradas: " + oficina.getCaixa().getOrdensServico().size());
    
        /*try {
            oficina.salvarDados();
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados de demonstração: " + e.getMessage());
        }*/
    }
    
    /**
     * Incrementa o contador privado de veículos.
     * Este método é chamado sempre que um novo veículo é adicionado ao sistema.
     */
    private static void incrementarContadorVeiculosPrivate() {
        contadorVeiculosPrivate++;
    }
    
    /**
     * Retorna o número total de veículos cadastrados no sistema.
     * @return O número total de veículos cadastrados
     */
    public static int getContadorVeiculosPrivate() {
        return contadorVeiculosPrivate;
    }
}