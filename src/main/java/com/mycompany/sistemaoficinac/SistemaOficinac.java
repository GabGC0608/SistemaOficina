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
    
    /** Instância principal da oficina que gerencia todas as operações */
    private static Oficina oficina = new Oficina();
    
    /** Scanner para leitura de dados do console */
    private static Scanner scanner = new Scanner(System.in);
    
    /** Gerenciador de login e autenticação do sistema */
    private static Login loginManager = new Login(); // Inicializa com um objeto padrão
    
    /** Contador do total de veículos cadastrados no sistema */
    private static int contadorVeiculosPrivate = 0;

    /**
     * Obtém a instância da oficina.
     * @return A instância da oficina
     */
    public static Oficina getOficina() {
        return oficina;
    }

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
            oficina.salvarDados(); // Salva os dados após inicialização
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            System.err.println("Erro ao carregar os dados: " + e.getMessage());
            System.out.println("Deseja criar um novo sistema? (S/N)");
            String resposta = scanner.nextLine().trim().toUpperCase();
            if ("S".equals(resposta)) {
                System.out.println("Criando novo sistema...");
                oficina = new Oficina();
                loginManager = new Login();
                oficina.inicializarDadosDemonstracao();
                try {
                    oficina.salvarDados(); // Salva os dados após inicialização
                } catch (IOException ex) {
                    System.err.println("Erro ao salvar os dados: " + ex.getMessage());
                }
            } else {
                System.out.println("Operação cancelada. O sistema será encerrado.");
                System.exit(0);
            }
        }


        

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
                            try {
                                oficina.salvarDados();
                                System.out.println("Dados salvos com sucesso!");
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
        
            System.out.println("\n=== MENU ELEVADORES ===");
            System.out.println("1. Listar elevadores");
            System.out.println("2. Alocar elevador");
            System.out.println("3. Liberar elevador");
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite a opção desejada: ");
            
            switch (opcao) {
                case 1:
                    Elevador.listarElevadores();
                    break;
                case 2:
                    oficina.alocarElevador();
                    break;
                case 3:
                    int numero = Oficina.lerInteiro("Digite o número do elevador a ser liberado (1-3): ");
                    if (numero >= 1 && numero <= 3) {
                        Elevador.liberarElevador(numero - 1);
                    } else {
                        System.out.println("Número inválido!");
                    }
                    break;
                case 0:
                    voltar       = true;
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
            System.out.println("2. Listar todos os funcionários");
            System.out.println("3. Buscar funcionário por nome");
            System.out.println("4. Demitir funcionário");
            System.out.println("5. Alterar dados do funcionário");
            System.out.println("6. Registrar ponto");
            System.out.println("7. Mostrar registros de ponto");
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
                    String nome = Oficina.lerString("Digite o nome do funcionário: ");
                    Funcionario funcionario = oficina.buscarFuncionarioPorNome(nome);
                    if (funcionario != null) {
                        System.out.println(funcionario);
                    } else {
                        System.out.println("Funcionário não encontrado!");
                    }
                    break;
                case 4:
                    oficina.demitirFuncionario();
                    break;
                case 5:
                    oficina.alterarDadosFuncionario();
                    break;
                case 6:
                    System.out.println("\n=== REGISTRAR PONTO ===");
                    System.out.println("1. Registrar entrada");
                    System.out.println("2. Registrar saída");
                    int opcaoPonto = Oficina.lerInteiro("Digite sua opção: ");
                    if (opcaoPonto == 1) {
                        oficina.registrarPonto(true);
                    } else if (opcaoPonto == 2) {
                        oficina.registrarPonto(false);
                    } else {
                        System.out.println("Opção inválida!");
                    }
                    break;
                case 7:
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
            System.out.println("\n=== MENU ORDENS DE SERVICO ===");
            System.out.println("1. Agendar serviço");
            System.out.println("2. Cancelar ordem de serviço");
            System.out.println("3. Concluir ordem   de serviço");
            System.out.println("4. Listar todos agendamentos");
            System.out.println("5. Listar agendamentos por cliente");
            System.out.println("6. Listar agendamentos por status");
            System.out  .println("7. Adicionar peça a ordem de serviço");    
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite sua opção: ");
            switch (opcao) {
                case 1:
                    oficina.agendarServico();
                    break;
                case 2:
                    oficina.cancelarOrdemServico();
                    break;
                case 3:
                    oficina.concluirOrdemServico();
                    break;
                case 4:
                    oficina.listarOrdens();
                    break;
                case 5:
                    oficina.listarOrdensPorCliente();
                    break;
                case 6:
                    oficina.listarOrdensPorStatus();
                    break;
                case 7:
                    oficina.adicionarPecaAOrdemServico();
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
        while (true) {
            System.out.println("\n=== MENU FINANCEIRO ===");
            System.out.println("1. Registrar entrada");
            System.out.println("2. Registrar saída");
            System.out.println("3. Gerar balanço mensal");
            System.out.println("0. Voltar");
            
            int opcao = Oficina.lerInteiro("Digite a opção desejada: ");
            
            switch (opcao) {
                case 1:
                    oficina.registrarEntrada();
                    break;
                case 2:
                    oficina.registrarSaida();
                    break;
                case 3:
                    oficina.gerarBalancoMensal();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
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