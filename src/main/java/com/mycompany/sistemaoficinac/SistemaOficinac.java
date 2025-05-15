

package com.mycompany.sistemaoficinac;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
            carregarDados();    // Carrega os dados da oficina e do login
            
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
                int opcao = lerInteiro("Digite sua opção: ");
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
                        alterarSenha(); 
                        break;
                    case 0:
                        try {
                            salvarDados();
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
        } else if (tipoUsuario.equals("Funcionario")) {
            boolean voltar = false;
            while (!voltar) {
                exibirMenuFuncionario();
                int opcao = lerInteiro("Digite sua opção: ");
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
                            salvarDados();
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
                case 5:
                    venderItemEstoque();
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
            
            int opcao = lerInteiro("Digite sua opção: ");
            switch (opcao) {
                case 1:
                    registrarPonto(true);
                    break;
                case 2:
                    registrarPonto(false);
                    break;
                case 3:
                    listarRegistrosPonto();
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
            
            int opcao = lerInteiro("Digite sua opção: ");
            switch (opcao) {
                case 1:
                    Elevador.listarElevadores();
                    break;
                case 2:
                    alocarElevador();
                    break;
                case 3:
                    liberarElevador();
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
                case 6:
                    alterarDadosCliente();
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
    /**
     * Exibe o menu de opções para gerenciar funcionários.
     */
    private static void menuFuncionarios() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n=== MENU FUNCIONÁRIOS ===");
            System.out.println("1. Cadastrar novo funcionário");
            System.out.println("2. Listar todos os funcionários");
            System.out.println("3. Buscar funcionário por nome");
            System.out.println("4. Remover funcionário");
            System.out.println("5. Alterar dados do funcionário");
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
                case 5:
                    alterarDadosFuncionario();
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
            System.out.println("5. Gerar relatório mensal");
            System.out.println("0. Voltar");
            
            int opcao = lerInteiro("Digite sua opção: ");
            
            switch (opcao) {
                case 1:
                    registrarPagamento();
                    break;
                case 2:
                    registrarDespesa();
                    break;
                case 3:
                    registrarCompraPecas();
                    break;
                case 4:
                    registrarPagamentoSalarios();
                    break;
                case 5:
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
    /***
     * Registra o pagamento de um cliente.
     */
    private static void registrarCompraPecas() {
        System.out.println("\n=== REGISTRAR COMPRA DE PEÇAS ===");
        String codigo = lerString("Código da peça: ");
        String nome = lerString("Nome da peça: ");
        int quantidade = lerInteiro("Quantidade: ");
        double precoUnitario = lerDouble("Preço unitário: ");
        String data = lerString("Data da compra (dd/MM/yyyy): ");
        
        oficina.registrarCompraPecas(codigo, nome, quantidade, precoUnitario, data);
    }

    /***
     * Registra o pagamento de um cliente.  
     */
    private static void registrarPagamentoSalarios() {
        String data = lerString("Digite a data do pagamento (dd/MM/yyyy): ");
        oficina.registrarPagamentoSalarios(data);
    }
    /**
     * Exibe o menu de opções para gerar relatórios.
     */
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
    /***
     * Aloca o elevador
     */
    private static void alocarElevador() {
        System.out.println("\n=== ALOCAR ELEVADOR ===");
        Elevador.listarElevadores();
        int numero = lerInteiro("Digite o número do elevador a ser alocado (1-3): ");
        
        if (numero < 1 || numero > 3) {
            System.out.println("Número inválido!");
            return;
        }
        
        Elevador elevador = Elevador.alocarElevador(numero - 1); // Índice base 0
        if (elevador != null) {
            String modeloVeiculo = lerString("Digite o modelo do veículo para o elevador: ");
            elevador.setModelo(modeloVeiculo);
            System.out.println("Elevador " + numero + " alocado para o veículo: " + modeloVeiculo);
        }
    }
    /***
     *  Libera o elevador
     */
    private static void liberarElevador() {
        System.out.println("\n=== LIBERAR ELEVADOR ===");
        Elevador.listarElevadores();
        int numero = lerInteiro("Digite o número do elevador a ser liberado (1-3): ");
        
        if (numero < 1 || numero > 3) {
            System.out.println("Número inválido!");
            return;
        }
        
        Elevador.liberarElevador(numero - 1); // Índice base 0
        System.out.println("Elevador " + numero + " liberado com sucesso!");
    }
    /**
     * Adiciona um item ao estoque da oficina.
     */
    private static void venderItemEstoque() {
        System.out.println("\n=== VENDER ITEM ===");
        String codigo = lerString("Código: ");
        int quantidade = lerInteiro("Quantidade: ");
        
        oficina.venderItemEstoque(codigo, quantidade);
    } 
    private static void adicionarItemEstoque() {
        System.out.println("\n=== ADICIONAR ITEM ===");
        String codigo = lerString("Código: ");
        String nome = lerString("Nome: ");
        int quantidade = lerInteiro("Quantidade: ");
        
        oficina.adicionarItemEstoque(codigo, nome, quantidade);
    }
    /**
     * Remove um item do estoque da oficina.
     */
    private static void removerItemEstoque() {
        String codigo = lerString("Digite o código do item a remover: ");
        oficina.removerItemEstoque(codigo);
        System.out.println("Item removido do estoque!");
    }
    /**
     * Atualiza a quantidade de um item no estoque da oficina.
     */
    private static void atualizarItemEstoque() {
        String codigo = lerString("Digite o código do item: ");
        int quantidade = lerInteiro("Nova quantidade: ");
        oficina.atualizarItemEstoque(codigo, quantidade);
        System.out.println("Quantidade atualizada!");
    }
    /**
     * Cadastra um novo cliente na oficina.
     */    
    
    private static void cadastrarCliente() {
        System.out.println("\n=== CADASTRAR CLIENTE ===");
        String nome = lerString("Nome: ");
        String telefone = lerString("Telefone: ");
        String endereco = lerString("Endereço: ");
        String cpf = lerString("CPF (11 dígitos): ");

    


        String cpfAnonimizado = oficina.anonimizarCPF(cpf);
        
        Cliente novoCliente = new Cliente(nome, telefone, endereco, null, cpfAnonimizado);
        oficina.cadastrarCliente(novoCliente);
       
    }
    /**
     * Busca um cliente pelo nome.
     */
    private static void buscarClientePorNome() {
        String nome = lerString("Digite o nome do cliente: ");
        oficina.getClientes().stream()
            .filter(c -> c.getNome().equalsIgnoreCase(nome))
            .forEach(System.out::println);
    }
    /**
     * Remove um cliente da oficina.
     */
    private static void removerCliente() {
        String telefone = lerString("Digite o telefone do cliente a ser removido: ");
        oficina.removerCliente(telefone);
    }
    /**
     * Adiciona um veículo a um cliente existente.
     */
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
    /***
     * Método que retorna o contador de veículos.
     */
    protected static void incrementarContadorVeiculos() {
        contadorVeiculos++;
    }
    /***
     * Método que retorna o contador de veículos.
     * @return contadorVeiculos
     * @see #incrementarContadorVeiculos()
     */
    public static int getContadorVeiculos() {
        return contadorVeiculos;
    }
    /**
     * Lista todos os veículos cadastrados na oficina.
     */
    /**
 * Lista todos os veículos cadastrados na oficina.
 */
    private static void listarTodosVeiculos() {
        System.out.println("\n=== TODOS OS VEÍCULOS ===");
        boolean encontrouVeiculos = false;

        for (Cliente cliente : oficina.getClientes()) {
            if (cliente.getVeiculos() != null && !cliente.getVeiculos().isEmpty()) {
                System.out.println("\nCliente: " + cliente.getNome());
                for (Veiculo veiculo : cliente.getVeiculos()) {
                    System.out.println("Modelo: " + veiculo.getModelo() + ", Placa: " + veiculo.getPlaca());
                }
                encontrouVeiculos = true;
        }
    }

        if (!encontrouVeiculos) {
            System.out.println("Nenhum veículo cadastrado.");
        }
    }
    /**
     * Busca um veículo pelo número da placa.
     */
    private static void buscarVeiculoPorPlaca() {
        String placa = lerString("Digite a placa do veículo: ");
        oficina.getClientes().stream()
            .flatMap(cliente -> cliente.getVeiculos().stream())
            .filter(v -> ((Veiculo)v).getPlaca().equalsIgnoreCase(placa))
            .forEach(System.out::println);
    }
    /**
     * Cadastra um novo funcionário na oficina.
     */
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
    /**
 * Altera os dados de um cliente existente.
 */
    private static void alterarDadosCliente() {
        System.out.println("\n=== ALTERAR DADOS DO CLIENTE ===");
        String telefone = lerString("Digite o telefone do cliente: ");
        
        // Busca o cliente pelo telefone
        Cliente cliente = oficina.getClientes().stream()
            .filter(c -> c.getTelefone().equals(telefone))
            .findFirst()
            .orElse(null);
        
        if (cliente == null) {
            System.out.println("Cliente não encontrado!");
            return;
        }
        
        boolean alterar = true;
        while (alterar) {
            System.out.println("\nCliente encontrado: " + cliente);
            System.out.println("1. Alterar nome");
            System.out.println("2. Alterar telefone");
            System.out.println("3. Alterar endereço");
            System.out.println("4. Alterar ID do cliente");
            System.out.println("0. Voltar");
            
            int opcao = lerInteiro("Digite sua opção: ");
            switch (opcao) {
                case 1:
                    String novoNome = lerString("Novo nome: ");
                    cliente.setNome(novoNome);
                    break;
                case 2:
                    String novoTelefone = lerString("Novo telefone: ");
                    cliente.setTelefone(novoTelefone);
                    break;
                case 3:
                    String novoEndereco = lerString("Novo endereço: ");
                    cliente.setEndereco(novoEndereco);
                    break;
                case 4:
                    String novoId = lerString("Novo ID do cliente: ");
                    cliente.setClienteId(novoId);
                    break;
                case 0:
                    alterar = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        System.out.println("Dados do cliente atualizados com sucesso!");
    }
    /**
 * Altera os dados de um funcionário existente.
 */
    private static void alterarDadosFuncionario() {
        System.out.println("\n=== ALTERAR DADOS DO FUNCIONÁRIO ===");
        String matricula = lerString("Digite a matrícula do funcionário: ");
        
        // Busca o funcionário pela matrícula
        Funcionario funcionario = oficina.getFuncionarios().stream()
            .filter(f -> f.getMatricula().equalsIgnoreCase(matricula))
            .findFirst()
            .orElse(null);
        
        if (funcionario == null) {
            System.out.println("Funcionário não encontrado!");
            return;
        }
        
        boolean alterar = true;
        while (alterar) {
            System.out.println("\nFuncionário encontrado: " + funcionario);
            System.out.println("1. Alterar nome");
            System.out.println("2. Alterar telefone");
            System.out.println("3. Alterar endereço");
            System.out.println("4. Alterar cargo");
            System.out.println("5. Alterar salário");
            System.out.println("0. Voltar");
            
            int opcao = lerInteiro("Digite sua opção: ");
            switch (opcao) {
                case 1:
                    String novoNome = lerString("Novo nome: ");
                    funcionario.setNome(novoNome);
                    break;
                case 2:
                    String novoTelefone = lerString("Novo telefone: ");
                    funcionario.setTelefone(novoTelefone);
                    break;
                case 3:
                    String novoEndereco = lerString("Novo endereço: ");
                    funcionario.setEndereco(novoEndereco);
                    break;
                case 4:
                    String novoCargo = lerString("Novo cargo: ");
                    funcionario.setCargo(novoCargo);
                    break;
                case 5:
                    double novoSalario = lerDouble("Novo salário: ");
                    funcionario.setSalario(novoSalario);
                    break;
                case 0:
                    alterar = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        System.out.println("Dados do funcionário atualizados com sucesso!");
    }
    
    /***
     * Registra o ponto do funcionário.
     * <p>
     * @param isEntrada
     */
    private static void registrarPonto(boolean isEntrada) {
        String matricula = lerString("Digite a matrícula: ");
        oficina.registrarPonto(matricula, isEntrada);
        System.out.println(isEntrada ? "Entrada registrada!" : "Saída registrada!");
    }

    /***
     * Lista os registros de ponto dos funcionários.
     * <p>
     */
    private static void listarRegistrosPonto() {
        System.out.println("\n=== REGISTROS DE PONTO ===");
        oficina.getRegistrosPonto().forEach(System.out::println);
    }

    /**
     * Busca um funcionário pelo nome.
     */
    private static void buscarFuncionarioPorNome() {
        String nome = lerString("Digite o nome do funcionário: ");
        oficina.getFuncionarios().stream()
            .filter(f -> f.getNome().equalsIgnoreCase(nome))
            .forEach(System.out::println);
    }


    /**
     * Remove um funcionário da oficina.
     */
    private static void removerFuncionario() {
        String matricula = lerString("Digite a matrícula do funcionário a ser removido: ");
        oficina.demitirFuncionario(matricula);
    }

    /**
     * Cadastra um novo serviço na oficina.
     */
    private static void cadastrarServico() {
        System.out.println("\n=== CADASTRAR SERVIÇO ===");
        String nome = lerString("Nome: ");
        String descricao = lerString("Descrição: ");
        double valor = lerDouble("Valor: ");
        int tempo = lerInteiro("Tempo estimado (minutos): ");
        
        Servico novoServico = new Servico(nome, descricao, valor, tempo);
        oficina.adicionarServico(novoServico);
    }
    /**
     * Remove um serviço da oficina.
     */
    private static void removerServico() {
        String nome = lerString("Digite o nome do serviço a ser removido: ");
        oficina.removerServico(nome);
    }
    /**
     * Realiza um agendamento de serviço na oficina.
     */
    private static void realizarAgendamento() {
        System.out.println("\n=== REALIZAR AGENDAMENTO ===");

        // Listar clientes para seleção
        System.out.println("\nClientes disponíveis:");
        oficina.listarClientes();
        String clienteId = lerString("Digite o ID do cliente: ");
        Cliente cliente = oficina.getClientes().stream()
            .filter(c -> c.getClienteId() != null && c.getClienteId().equalsIgnoreCase(clienteId))
            .findFirst()
            .orElse(null);

        if (cliente == null) {
            System.out.println("Cliente não encontrado!");
            return;
        }

        // Listar veículos do cliente
        System.out.println("\nVeículos do cliente:");
        if (cliente.getVeiculos().isEmpty()) {
            System.out.println("Este cliente não possui veículos cadastrados!");
            return;
        }
        cliente.listarVeiculos();

        String placa = lerString("Digite a placa do veículo (ex: ABC1D23): ");
        Veiculo veiculo = cliente.getVeiculos().stream()
            .filter(v -> v.getPlaca() != null && v.getPlaca().equalsIgnoreCase(placa.replace(" ", "")))
            .findFirst()
            .orElse(null);

        if (veiculo == null) {
            System.out.println("Veículo não encontrado! Verifique a placa digitada.");
            return;
        }

        // Selecionar múltiplos serviços
        List<Servico> servicosSelecionados = new ArrayList<>();
        boolean adicionarMaisServicos = true;
        
        while (adicionarMaisServicos) {
            System.out.println("\nServiços disponíveis:");
            List<Servico> servicos = oficina.getServicos();
            for (int i = 0; i < servicos.size(); i++) {
                System.out.println((i+1) + ". " + servicos.get(i).getNome() + " - R$" + servicos.get(i).getValor());
            }

            int opcaoServico = lerInteiro("Digite o número do serviço a adicionar: ");
            if (opcaoServico < 1 || opcaoServico > servicos.size()) {
                System.out.println("Número de serviço inválido!");
                continue;
            }
            
            servicosSelecionados.add(servicos.get(opcaoServico - 1));
            System.out.println("Serviço adicionado: " + servicos.get(opcaoServico - 1).getNome());

            String resposta = lerString("Deseja adicionar outro serviço? (S/N): ");
            adicionarMaisServicos = resposta.equalsIgnoreCase("S");
        }

        if (servicosSelecionados.isEmpty()) {
            System.out.println("Nenhum serviço selecionado!");
            return;
        }

        // Listar funcionários disponíveis
        System.out.println("\nFuncionários disponíveis:");
        oficina.listarFuncionarios();
        String matriculaFuncionario = lerString("Digite a matrícula do funcionário: ");
        Funcionario funcionario = oficina.getFuncionarios().stream()
            .filter(f -> f.getMatricula() != null && f.getMatricula().equalsIgnoreCase(matriculaFuncionario))
            .findFirst()
            .orElse(null);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado!");
            return;
        }

        // Solicitar data e hora
        String data = lerData("Digite a data do agendamento (dd/MM/yyyy): ");
        String hora = lerHora("Digite a hora do agendamento (HH:mm): ");
        String dataHora = data + " " + hora;

        // Verificar disponibilidade do funcionário
        if (!oficina.getAgenda().verificarDisponibilidadeFuncionario(funcionario, dataHora)) {
            System.out.println("Este funcionário já possui um agendamento neste horário!");
            return;
        }

        String status = "A fazer";
        oficina.agendarServico(cliente, veiculo, servicosSelecionados, funcionario, dataHora, status);
    }

    // Métodos auxiliares para validar data e hora
    private static String lerData(String mensagem) {
        while (true) {
            String input = lerString(mensagem);
            if (input.matches("\\d{2}/\\d{2}/\\d{4}")) {
                return input;
            }
            System.out.println("Formato inválido! Use dd/MM/yyyy (ex: 15/05/2025)");
        }
    }

    private static String lerHora(String mensagem) {
        while (true) {
            String input = lerString(mensagem);
            if (input.matches("\\d{2}:\\d{2}")) {
                return input;
            }
            System.out.println("Formato inválido! Use HH:mm (ex: 14:30)");
        }
    }
    /**
     * Lista os agendamentos por cliente.
     */
    private static void listarAgendamentosPorCliente() {
        String nome = lerString("Digite o nome do cliente: ");
        oficina.getAgenda().listarAgendamentosPorCliente(nome);
    }
    /**
     * Lista os agendamentos por status.
     */
    private static void listarAgendamentosPorStatus() {
        String status = lerString("Digite o status (Agendado/Concluído/Cancelado): ");
        oficina.getAgenda().listarAgendamentosPorStatus(status);
    }
        /**
     * Cancela um agendamento existente.
     */
    private static void cancelarAgendamento() {
        // Listar agendamentos primeiro
        oficina.getAgenda().listarAgendamentos();
        
        // Verificar se há agendamentos
        if (oficina.getAgenda().getAgendamentos().isEmpty()) {
            System.out.println("Não há agendamentos para cancelar.");
            return;
        }
        
        int index = lerInteiro("Digite o número do agendamento a cancelar: ") - 1;
        
        // Verificar se o índice é válido
        if (index < 0 || index >= oficina.getAgenda().getAgendamentos().size()) {
            System.out.println("Número de agendamento inválido!");
            return;
        }
        
        // Verificar status do agendamento
        Agenda.Agendamento ag = oficina.getAgenda().getAgendamentos().get(index);
        if ("Concluído".equalsIgnoreCase(ag.getStatus())) {
            System.out.println("Não é possível cancelar um agendamento já concluído!");
            return;
        }
        
        // Mostrar detalhes e confirmar
        System.out.println("\nAgendamento selecionado:");
        System.out.println(ag);
        String confirmacao = lerString("Deseja realmente cancelar este agendamento? (S/N): ");
        
        if (confirmacao.equalsIgnoreCase("S")) {
            // Chama o método da classe Oficina para cancelar
            oficina.cancelarAgendamento(index);
        } else {
            System.out.println("Cancelamento não realizado.");
        }
    }

    
    /**
     * Conclui um agendamento existente.
     */
    private static void concluirAgendamento() {
        // Listar agendamentos primeiro
        oficina.getAgenda().listarAgendamentos();
        
        // Verificar se há agendamentos
        if (oficina.getAgenda().getAgendamentos().isEmpty()) {
            System.out.println("Não há agendamentos para concluir.");
            return;
        }
        
        int index = lerInteiro("Digite o número do agendamento a concluir: ") - 1;
        
        // Verificar se o índice é válido
        if (index < 0 || index >= oficina.getAgenda().getAgendamentos().size()) {
            System.out.println("Número de agendamento inválido!");
            return;
        }
        
        // Obter o agendamento
        Agenda.Agendamento ag = oficina.getAgenda().getAgendamentos().get(index);
        
        // Verificar status do agendamento
        if ("Concluído".equalsIgnoreCase(ag.getStatus())) {
            System.out.println("Este agendamento já foi concluído!");
            return;
        }
        
        if ("Cancelado".equalsIgnoreCase(ag.getStatus())) {
            System.out.println("Não é possível concluir um agendamento cancelado!");
            return;
        }
        
        // Mostrar detalhes e confirmar
        System.out.println("\nAgendamento selecionado:");
        System.out.println(ag);
        String confirmacao = lerString("Deseja realmente concluir este agendamento? (S/N): ");
        
        if (confirmacao.equalsIgnoreCase("S")) {
            // Conclui o agendamento
            oficina.getAgenda().concluirAgendamento(index);
            
            // Registrar pagamento apenas se não estiver concluído
            if (!"Concluído".equalsIgnoreCase(ag.getStatus())) {
                String servicosStr = ag.getServicos().stream()
                                    .map(Servico::getNome)
                                    .collect(Collectors.joining(", "));
                
                String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                oficina.registrarPagamento(ag.getValorTotal(), 
                                        "Serviços: " + servicosStr + " - Cliente: " + ag.getCliente().getNome(), 
                                        dataAtual);
                
                System.out.println("Agendamento concluído e pagamento registrado com sucesso!");
            }
        } else {
            System.out.println("Conclusão não realizada.");
        }
    }
    /**
     * Registra um pagamento na oficina.
     */
    private static void registrarPagamento() {
        double valor = lerDouble("Valor: ");
        String descricao = lerString("Descrição: ");
        String data = lerString("Data (dd/MM/yyyy): ");
        oficina.registrarPagamento(valor, descricao, data);
    }
    /**
     * Registra uma despesa na oficina.
     */
    private static void registrarDespesa() {
        double valor = lerDouble("Valor: ");
        String descricao = lerString("Descrição: ");
        String data = lerString("Data (dd/MM/yyyy): ");
        oficina.registrarDespesa(valor, descricao, data);
    }
    /**
     * Gera um relatório diário de serviços realizados.
     */
    private static void gerarRelatorioDiario() {
        String data = lerString("Digite a data (dd/MM/yyyy): ");
        oficina.gerarRelatorioDiario(data);
    }
    /**
     * Gera um relatório mensal de serviços realizados.
     */
    private static void gerarRelatorioMensal() {
        int mes = lerInteiro("Digite o mês (1-12): ");
        int ano = lerInteiro("Digite o ano: ");
        oficina.gerarRelatorioMensal(mes, ano);
    }
    /**
 * Salva os dados de todas as classes em arquivos JSON separados.
 * @throws IOException Se ocorrer um erro durante a escrita nos arquivos.
 */
    public static void salvarDados() throws IOException {
        // Usando a classe JsonUtil que já tem a configuração de pretty print
        JsonUtil.salvarParaJson(oficina, "data/oficina.json");
        System.out.println("Dados da oficina salvos com sucesso!");

        JsonUtil.salvarParaJson(oficina.getRegistrosPonto(), "data/pontos.json");
        System.out.println("Dados de ponto salvos com sucesso!");

        JsonUtil.salvarParaJson(loginManager, "data/login.json");
        System.out.println("Dados de login salvos com sucesso!");

        JsonUtil.salvarParaJson(contadorVeiculos, "data/contadorVeiculos.json");
        System.out.println("Contador de veículos salvo com sucesso!");

        JsonUtil.salvarParaJson(oficina.getClientes(), "data/clientes.json");
        System.out.println("Dados dos clientes salvos com sucesso!");

        JsonUtil.salvarParaJson(oficina.getFuncionarios(), "data/funcionarios.json");
        System.out.println("Dados dos funcionários salvos com sucesso!");

        JsonUtil.salvarParaJson(oficina.getServicos(), "data/servicos.json");
        System.out.println("Dados dos serviços salvos com sucesso!");

        JsonUtil.salvarParaJson(oficina.getEstoque(), "data/estoque.json");
        System.out.println("Dados do estoque salvos com sucesso!");

        JsonUtil.salvarParaJson(oficina.getAgenda(), "data/agenda.json");
        System.out.println("Dados da agenda salvos com sucesso!");

        JsonUtil.salvarParaJson(oficina.getCaixa(), "data/caixa.json");
        System.out.println("Dados do caixa salvos com sucesso!");
    }


/**
 * Carrega os dados de todas as classes a partir de arquivos JSON separados.
 * @throws IOException Se ocorrer um erro durante a leitura dos arquivos.
 * @throws ClassNotFoundException Se ocorrer um erro na desserialização dos objetos.
 */
    private static void carregarDados() throws IOException, ClassNotFoundException {
        File arquivoOficina = new File("data/oficina.json");
        File arquivoLogin = new File("data/login.json");
        File arquivoClientes = new File("data/clientes.json");
        File arquivoFuncionarios = new File("data/funcionarios.json");
        File arquivoServicos = new File("data/servicos.json");
        File arquivoEstoque = new File("data/estoque.json");
        File arquivoAgenda = new File("data/agenda.json");
        File arquivoCaixa = new File("data/caixa.json");
        File arquivoPonto = new File("data/pontos.json");

        ObjectMapper mapper = new ObjectMapper();

        if (!arquivoOficina.exists() || !arquivoLogin.exists()) {
            System.out.println("Arquivos de dados não encontrados. Criando novo sistema...");
            oficina = new Oficina();
            loginManager = new Login();
            contadorVeiculos = 0;
            return;
        }

        // Carrega os dados da oficina
        oficina = mapper.readValue(arquivoOficina, Oficina.class);
        System.out.println("Dados da oficina carregados com sucesso!");

        // Carrega os dados de login
        loginManager = mapper.readValue(arquivoLogin, Login.class);
        System.out.println("Dados de login carregados com sucesso!");

        // Carrega o contador de veículos
        File arquivoContador = new File("data/contadorVeiculos.json");
        if (arquivoContador.exists()) {
            contadorVeiculos = mapper.readValue(arquivoContador, Integer.class);
            System.out.println("Contador de veículos carregado com sucesso!");
        } else {
            contadorVeiculos = 0;
        }

        // Carrega os dados de clientes
        if (arquivoClientes.exists()) {
            List<Cliente> clientes = mapper.readValue(arquivoClientes, new TypeReference<List<Cliente>>() {});
            oficina.setClientes(clientes);
            System.out.println("Dados dos clientes carregados com sucesso!");
        } else {
            oficina.setClientes(new ArrayList<>());
        }

        // Carrega os dados de funcionários
        if (arquivoFuncionarios.exists()) {
            List<Funcionario> funcionarios = mapper.readValue(arquivoFuncionarios, new TypeReference<List<Funcionario>>() {});
            oficina.setFuncionarios(funcionarios);
            System.out.println("Dados dos funcionários carregados com sucesso!");
        } else {
            oficina.setFuncionarios(new ArrayList<>());
        }

        // Carrega os dados de serviços
        if (arquivoServicos.exists()) {
            List<Servico> servicos = mapper.readValue(arquivoServicos, new TypeReference<List<Servico>>() {});
            oficina.setServicos(servicos);
            System.out.println("Dados dos serviços carregados com sucesso!");
        } else {
            oficina.setServicos(new ArrayList<>());
        }

        // Carrega os dados do estoque
        if (arquivoEstoque.exists()) {
            Estoque estoque = mapper.readValue(arquivoEstoque, Estoque.class);
            oficina.setEstoque(estoque);
            System.out.println("Dados do estoque carregados com sucesso!");
        } else {
            oficina.setEstoque(new Estoque());
        }

        // Carrega os dados da agenda
        if (arquivoAgenda.exists()) {
            Agenda agenda = mapper.readValue(arquivoAgenda, Agenda.class);
            oficina.setAgenda(agenda);
            System.out.println("Dados da agenda carregados com sucesso!");
        } else {
            oficina.setAgenda(new Agenda());
        }

        // Carrega os dados do caixa
        if (arquivoCaixa.exists()) {
            Caixa caixa = mapper.readValue(arquivoCaixa, Caixa.class);
            oficina.setCaixa(caixa);
            System.out.println("Dados do caixa carregados com sucesso!");
        } else {
            oficina.setCaixa(new Caixa());
        }
    }
    
    /***
     * Altera a senha do usuário.
     */
    private static void alterarSenha() {
        System.out.println("\n=== ALTERAR SENHA ===");
        String usuario = lerString("Digite o nome de usuário: ");
        String senhaAtual = lerString("Digite a senha atual: ");
        String novaSenha = lerString("Digite a nova senha: ");
        
        // Chama o método da classe Login para alterar a senha
        loginManager.alterarSenha(usuario, senhaAtual, novaSenha);
    }  
    // Métodos auxiliares para leitura de entrada
    /**
     * Lê uma string do console.
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return A string lida do console
     */
    private static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }
    /**
     * Lê um inteiro do console.
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return O inteiro lido do console
     */
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
    /**
     * Lê um número decimal do console.
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return O número decimal lido do console
     */
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

        /**
     * Inicializa dados de demonstração para facilitar o uso do sistema.
     * <p>
     * Este método cria funcionários, clientes, veículos, serviços e agendamentos de exemplo
     * para demonstrar as funcionalidades do sistema.
     * </p>
     */
    private static void inicializarDadosDemonstracao() {
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

    
}