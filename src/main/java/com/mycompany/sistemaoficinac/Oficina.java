package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa a oficina mecânica.
 * Gerencia elevadores, funcionários, clientes, serviços, agenda, estoque e caixa.
 */
public class Oficina {
    private Elevador[] elevadores; // Array de elevadores da oficina
    private List<Funcionario> funcionarios; // Lista de funcionários
    private List<Cliente> clientes; // Lista de clientes
    private List<Servico> servicos; // Lista de serviços disponíveis
    private Agenda agenda; // Agenda de serviços
    private Estoque estoque; // Estoque de itens
    private Caixa caixa; // Caixa financeiro da oficina

    /**
     * Construtor da classe Oficina.
     * Inicializa os atributos com valores padrão.
     */
    public Oficina() {
        this.elevadores = new Elevador[3]; // Supondo que a oficina tenha 3 elevadores
        this.funcionarios = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.servicos = new ArrayList<>();
        this.agenda = new Agenda();
        this.caixa = new Caixa();
        this.estoque = new Estoque();
        inicializarServicosPadrao();
    }

    /**
     * Inicializa a lista de serviços com serviços padrão.
     */
    private void inicializarServicosPadrao() {
        servicos.add(new Servico("Troca de Óleo", "Troca de óleo e filtro", 120.00, 30));
        servicos.add(new Servico("Revisão Básica", "Revisão de 10 itens", 250.00, 60));
        servicos.add(new Servico("Revisão Completa", "Revisão de 30 itens", 450.00, 120));
        servicos.add(new Servico("Alinhamento", "Alinhamento e balanceamento", 180.00, 45));
    }

    // Métodos para gerenciar o estoque
    /**
     * Adiciona um item ao estoque.
     *
     * @param codigo Código do item.
     * @param nome Nome do item.
     * @param quantidade Quantidade do item.
     */
    public void adicionarItemEstoque(String codigo, String nome, int quantidade) {
        estoque.adicionarItem(codigo, nome, quantidade);
    }

    /**
     * Remove um item do estoque.
     *
     * @param codigo Código do item a ser removido.
     */
    public void removerItemEstoque(String codigo) {
        estoque.removerItem(codigo);
    }

    /**
     * Atualiza a quantidade de um item no estoque.
     *
     * @param codigo Código do item.
     * @param novaQuantidade Nova quantidade do item.
     */
    public void atualizarItemEstoque(String codigo, int novaQuantidade) {
        estoque.atualizarQuantidade(codigo, novaQuantidade);
    }

    /**
     * Lista todos os itens do estoque.
     */
    public void listarEstoque() {
        estoque.listarItens();
    }

    /**
     * Obtém o estoque da oficina.
     *
     * @return Estoque da oficina.
     */
    public Estoque getEstoque() {
        return estoque;
    }

    // Métodos para gerenciar elevadores
    /**
     * Obtém um elevador disponível.
     *
     * @return Elevador disponível ou null se todos estiverem ocupados.
     */
    private Elevador obterElevadorDisponivel() {
        for (Elevador elevador : elevadores) {
            if (elevador.getPeso() == 0) { // Elevador vazio
                return elevador;
            }
        }
        return null; // Nenhum elevador disponível
    }

    /**
     * Libera um elevador após o serviço.
     *
     * @param elevador Elevador a ser liberado.
     */
    public void liberarElevador(Elevador elevador) {
        elevador.setPeso(0); // Zera o peso do elevador
        elevador.setModelo(null); // Remove o modelo do carro
    }

    /**
     * Agenda um serviço para um cliente.
     *
     * @param cliente Cliente do serviço.
     * @param veiculo Veículo do cliente.
     * @param nomeServico Nome do serviço.
     * @param responsavel Funcionário responsável.
     * @param dataHora Data e hora do serviço.
     * @param status Status do serviço.
     */
    public void agendarServico(Cliente cliente, Veiculo veiculo, String nomeServico, Funcionario responsavel, String dataHora, String status) {
        Servico servico = servicos.stream()
            .filter(s -> s.getNome().equalsIgnoreCase(nomeServico))
            .findFirst()
            .orElse(null);

        if (servico != null) {
            if (servico.getNome().toLowerCase().contains("elevador")) { // Verifica se o serviço precisa de elevador
                Elevador elevadorDisponivel = obterElevadorDisponivel();
                if (elevadorDisponivel != null) {
                    //elevadorDisponivel.setPeso(veiculo.getPeso()); // Atribui o peso do veículo ao elevador
                    elevadorDisponivel.setModelo(veiculo.getModelo()); // Atribui o modelo do veículo ao elevador
                    System.out.println("Veículo atribuído ao " + elevadorDisponivel.getModelo());
                } else {
                    status = "Aguardo"; // Todos os elevadores estão ocupados
                    System.out.println("Todos os elevadores estão ocupados. Serviço em status de 'Aguardo'.");
                }
            }

            agenda.adicionarAgendamento(cliente, veiculo, servico, responsavel, dataHora, status);
            System.out.println("Serviço agendado com sucesso!");
        } else {
            System.out.println("Serviço não encontrado!");
        }
    }

    // Métodos para gerenciar funcionários
    /**
     * Contrata um novo funcionário.
     *
     * @param funcionario Funcionário a ser contratado.
     */
    public void contratarFuncionario(Funcionario funcionario) {
        funcionarios.add(funcionario);
        System.out.println("Funcionário " + funcionario.getNome() + " contratado com sucesso!");
    }

    /**
     * Demite um funcionário com base na matrícula.
     *
     * @param matricula Matrícula do funcionário a ser demitido.
     */
    public void demitirFuncionario(String matricula) {
        funcionarios.removeIf(f -> f.getMatricula().equals(matricula));
        System.out.println("Funcionário com matrícula " + matricula + " demitido com sucesso!");
    }

    /**
     * Lista todos os funcionários.
     */
    public void listarFuncionarios() {
        System.out.println("\n=== FUNCIONÁRIOS ===");
        funcionarios.forEach(System.out::println);
    }

    // Métodos para gerenciar clientes
    /**
     * Cadastra um novo cliente.
     *
     * @param cliente Cliente a ser cadastrado.
     */
    public void cadastrarCliente(Cliente cliente) {
        clientes.add(cliente);
        System.out.println("Cliente " + cliente.getNome() + " cadastrado com sucesso!");
    }

    /**
     * Remove um cliente com base no telefone.
     *
     * @param telefone Telefone do cliente a ser removido.
     */
    public void removerCliente(String telefone) {
        clientes.removeIf(c -> c.getTelefone().equals(telefone));
        System.out.println("Cliente com telefone " + telefone + " removido com sucesso!");
    }

    /**
     * Lista todos os clientes.
     */
    public void listarClientes() {
        System.out.println("\n=== CLIENTES ===");
        clientes.forEach(System.out::println);
    }

    // Métodos para gerenciar serviços
    /**
     * Adiciona um novo serviço à lista de serviços.
     *
     * @param servico Serviço a ser adicionado.
     */
    public void adicionarServico(Servico servico) {
        servicos.add(servico);
        System.out.println("Serviço " + servico.getNome() + " adicionado com sucesso!");
    }

    /**
     * Remove um serviço com base no nome.
     *
     * @param nomeServico Nome do serviço a ser removido.
     */
    public void removerServico(String nomeServico) {
        servicos.removeIf(s -> s.getNome().equalsIgnoreCase(nomeServico));
        System.out.println("Serviço " + nomeServico + " removido com sucesso!");
    }

    /**
     * Lista todos os serviços disponíveis.
     */
    public void listarServicos() {
        System.out.println("\n=== SERVIÇOS DISPONÍVEIS ===");
        servicos.forEach(System.out::println);
    }

    /**
     * Lista todos os agendamentos.
     */
    public void listarAgendamentos() {
        agenda.listarAgendamentos();
    }

    // Métodos para gerenciar finanças
    /**
     * Registra um pagamento no caixa.
     *
     * @param valor Valor do pagamento.
     * @param descricao Descrição do pagamento.
     * @param data Data do pagamento.
     */
    public void registrarPagamento(double valor, String descricao, String data) {
        caixa.registrarEntrada(valor, descricao, data);
    }

    /**
     * Registra uma despesa no caixa.
     *
     * @param valor Valor da despesa.
     * @param descricao Descrição da despesa.
     * @param data Data da despesa.
     */
    public void registrarDespesa(double valor, String descricao, String data) {
        caixa.registrarSaida(valor, descricao, data);
    }

    /**
     * Gera um relatório diário das transações financeiras.
     *
     * @param data Data do relatório.
     */
    public void gerarRelatorioDiario(String data) {
        caixa.gerarRelatorioDiario(data);
    }

    /**
     * Gera um relatório mensal das transações financeiras.
     *
     * @param mes Mês do relatório.
     * @param ano Ano do relatório.
     */
    public void gerarRelatorioMensal(int mes, int ano) {
        caixa.gerarRelatorioMensal(mes, ano);
    }

    // Getters
    /**
     * Obtém a lista de funcionários.
     *
     * @return Lista de funcionários.
     */
    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    /**
     * Obtém a lista de clientes.
     *
     * @return Lista de clientes.
     */
    public List<Cliente> getClientes() {
        return clientes;
    }

    /**
     * Obtém a lista de serviços.
     *
     * @return Lista de serviços.
     */
    public List<Servico> getServicos() {
        return servicos;
    }

    /**
     * Obtém a agenda de serviços.
     *
     * @return Agenda de serviços.
     */
    public Agenda getAgenda() {
        return agenda;
    }

    /**
     * Obtém o caixa financeiro.
     *
     * @return Caixa financeiro.
     */
    public Caixa getCaixa() {
        return caixa;
    }
}