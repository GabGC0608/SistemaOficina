

package com.mycompany.sistemaoficinac;
/**
 * Classe principal do sistema de gerenciamento de oficina mecânica.
 * <p>
 * Controla todo o fluxo do programa, incluindo menus, operações e persistência de dados.
 * </p>
 * 
 * @version 1.0
 * @author Gabriel
 */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SistemaOficinac {
    private static final String ARQUIVO_DADOS = "data/ficina.json";
    private static Oficina oficina = new Oficina();
    private static Scanner scanner = new Scanner(System.in);
    private static Login loginManager = new Login(); // Inicializa com um objeto padrão
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
            carregarDados(); // Carrega os dados da oficina e do login
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            System.err.println("Erro ao carregar os dados: " + e.getMessage());
            System.out.println("Deseja criar um novo sistema? (S/N)");
            String resposta = scanner.nextLine().trim().toUpperCase();
            if ("S".equals(resposta)) {
                System.out.println("Criando novo sistema...");
                oficina = new Oficina();
                loginManager = new Login();
            } else {
                System.out.println("Operação cancelada. O sistema será encerrado.");
                System.exit(0);
            }
        }
        System.out.println("=== SISTEMA DE GERENCIAMENTO DE OFICINA ===");
    
        // Solicita credenciais de login
        System.out.println("=== SISTEMA DE LOGIN ===");
        System.out.println("Login padrao: admin/admin123  ou  funcionario1/func123");
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
        
    
        // Direciona para o menu apropriado
        if (tipoUsuario.equals("Administrador")) {
            exibirMenuAdministrador();
        } else if (tipoUsuario.equals("Funcionario")) {
            exibirMenuFuncionario();
        } else {
            System.out.println("Tipo de usuário inválido!");
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
                        break;
                    case 6:
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
        System.out.println("4. Salvar e sair");
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
        System.out.println("6. Sair");
        
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
    /**
     * Adiciona um item ao estoque da oficina.
     */ 
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
        
        Cliente novoCliente = new Cliente(nome, telefone, endereco);
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
    /**
     * Lista todos os veículos cadastrados na oficina.
     */
    private static void listarTodosVeiculos() {
        System.out.println("\n=== TODOS OS VEÍCULOS ===");
        oficina.getClientes().forEach(cliente -> {
            System.out.println("\nCliente: " + cliente.getNome());
            cliente.listarVeiculos();
        });
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
        oficina.getAgenda().listarAgendamentos();
        int index = lerInteiro("Digite o número do agendamento a cancelar: ");
        oficina.getAgenda().cancelarAgendamento(index);
    }
    /**
     * Conclui um agendamento existente.
     */
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
     * Salva os dados da oficina e do login em um arquivo JSON.
     * @throws IOException Se ocorrer um erro durante a escrita no arquivo
     */
    public static void salvarDados() throws IOException {
        if (oficina == null || loginManager == null) {
            System.err.println("Erro: Dados inválidos. Não é possível salvar.");
            return;
        }

        Map<String, Object> dadosCompletos = new HashMap<>();
        dadosCompletos.put("oficina", oficina);
        dadosCompletos.put("login", loginManager);

        // Tenta salvar no arquivo principal
        JsonUtil.salvarParaJson(dadosCompletos, ARQUIVO_DADOS);
        System.out.println("Dados salvos com sucesso no arquivo: " + ARQUIVO_DADOS);
    }
    /**
 * Carrega os dados da oficina e do login a partir de um arquivo JSON.
 * @throws IOException Se ocorrer um erro durante a leitura do arquivo
 * @throws ClassNotFoundException Se ocorrer um erro na desserialização dos objetos
 */
    private static void carregarDados() throws IOException, ClassNotFoundException {
        File arquivo = new File(ARQUIVO_DADOS);
        
        if (!arquivo.exists()) {
            System.out.println("Arquivo não encontrado. Criando novo sistema...");
            oficina = new Oficina();
            loginManager = new Login();
            return;
        }

        Map<String, Object> dadosCompletos = JsonUtil.carregarDeJson(ARQUIVO_DADOS, Map.class);

        if (dadosCompletos.containsKey("oficina") && dadosCompletos.containsKey("login")) {
            oficina = JsonUtil.deJsonString(JsonUtil.paraJsonString(dadosCompletos.get("oficina")), Oficina.class);
            loginManager = JsonUtil.deJsonString(JsonUtil.paraJsonString(dadosCompletos.get("login")), Login.class);
            System.out.println("Dados carregados com sucesso!");
        } else {
            throw new IllegalArgumentException("Dados incompletos no arquivo JSON.");
        }
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

    // Método para inicializar dados de demonstração
   
    /**
     * Inicializa dados de demonstração para facilitar o uso do sistema.
     * <p>
     * Este método cria funcionários, clientes e serviços de exemplo para
     * demonstrar as funcionalidades do sistema.
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