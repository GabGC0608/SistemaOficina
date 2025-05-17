package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.sistemaoficinac.Agenda.Agendamento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    private List<PontoFuncionario> registrosPonto; // Lista de registros de ponto dos funcionários

    /**
     * Construtor da classe Oficina.
     * Inicializa os atributos com valores padrão.
     */
    public Oficina() {
        this.elevadores = new Elevador[3]; // Supondo que a oficina tenha 3 elevadores
        Elevador.inicializarElevadores();
        this.funcionarios = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.servicos = new ArrayList<>();
        this.agenda = new Agenda();
        this.caixa = new Caixa();
        this.estoque = new Estoque();
        this.registrosPonto = new ArrayList<>();
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
        servicos.add(new Servico("Troca de Pneus", "Troca de pneus e balanceamento", 600.00, 90));
        servicos.add(new Servico("Troca de Pastilhas", "Troca de pastilhas de freio", 200.00, 60));
        servicos.add(new Servico("Troca de Bateria", "Troca de bateria do veículo", 300.00, 45));
        servicos.add(new Servico("Troca de Filtro de Ar", "Troca do filtro de ar do motor", 100.00, 30));
        servicos.add(new Servico("Troca de Filtro de Combustível", "Troca do filtro de combustível", 150.00, 30));
        servicos.add(new Servico("Troca de Velas", "Troca de velas de ignição", 120.00, 30));
        servicos.add(new Servico("Troca de Correia Dentada", "Troca da correia dentada", 400.00, 120));
        servicos.add(new Servico("Troca de Fluido de Freio", "Troca do fluido de freio", 150.00, 30));    
    }
    /***
     * 
     * @param matricula
     * @param isEntrada
     */
    public void registrarPonto(String matricula, boolean isEntrada) {
        if (isEntrada) {
            registrosPonto.add(new PontoFuncionario(matricula, LocalDateTime.now(), null));
        } else {
            // Busca o último registro aberto para esta matrícula
            for (int i = registrosPonto.size() - 1; i >= 0; i--) {
                PontoFuncionario ponto = registrosPonto.get(i);
                if (ponto.getMatricula().equals(matricula) && ponto.getSaida() == null) {
                    ponto.setSaida(LocalDateTime.now());
                    break;
                }
            }
        }
    }
    /***
     * Retorna a lista de registros de ponto dos funcionários.
     * @return Lista de registros de ponto.
     */

    public List<PontoFuncionario> getRegistrosPonto() {
        return registrosPonto;
    }
    /***
     * Define a lista de registros de ponto dos funcionários.
     * @param registrosPonto
     */
    public void setRegistrosPonto(List<PontoFuncionario> registrosPonto) {
        this.registrosPonto = registrosPonto;
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
     * Vende um item do estoque.
     * @param codigo
     * @param quantidade
     */
    public void venderItemEstoque(String codigo, int quantidade) {
        estoque.venderItem(codigo, quantidade);
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
     * @param cliente Cliente que solicita o serviço.
     * @param veiculo Veículo do cliente.
     * @param servicos Lista de serviços a serem realizados.
     * @param responsavel Funcionário responsável pelo serviço.
     * @param dataHora Data e hora do agendamento.
     * @param status Status do agendamento.
     * 
     */
    public void agendarServico(Cliente cliente, Veiculo veiculo, List<Servico> servicos, Funcionario responsavel, String dataHora, String status) {
        // Verificar se há serviços que requerem elevador
        boolean requerElevador = servicos.stream()
            .anyMatch(s -> s.getNome().toLowerCase().contains("elevador"));

        if (requerElevador) {
            Elevador elevadorDisponivel = obterElevadorDisponivel();
            if (elevadorDisponivel != null) {
                elevadorDisponivel.setModelo(veiculo.getModelo());
                System.out.println("Veículo atribuído ao " + elevadorDisponivel.getModelo());
            } else {
                status = "Aguardo";
                System.out.println("Todos os elevadores estão ocupados. Serviço em status de 'Aguardo'.");
            }
        }

        agenda.adicionarAgendamento(cliente, veiculo, servicos, responsavel, dataHora, status);
        System.out.println("Serviço(s) agendado(s) com sucesso!");

        // Calcular e mostrar valor total
        double valorTotal = servicos.stream().mapToDouble(Servico::getValor).sum();
        System.out.printf("Valor total dos serviços: R$%.2f%n", valorTotal);
    }

    /**
     * Cancela um agendamento e registra o valor retido no caixa.
     *
     * @param index Índice do agendamento a ser cancelado.
     */
    public void cancelarAgendamento(int index, String matriculaFuncionario) {
        if (index >= 0 && index < agenda.getAgendamentos().size()) {
            Agendamento agendamento = agenda.getAgendamentos().get(index);
            double valorRetido = agendamento.getValorTotal() * 0.20;
            
            // Registrar a transação no caixa
            String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String descricao = "Cancelamento agendamento - Cliente: " + agendamento.getCliente().getNome();
            caixa.registrarEntrada(valorRetido, descricao, dataAtual, "Cancelamentos", matriculaFuncionario, null);
            
            // Remover o agendamento
            agenda.getAgendamentos().remove(index);
            System.out.println("Agendamento cancelado e valor retido registrado!");
        }
    }

    /***
     *  Conclui um agendamento e registra o pagamento no caixa.
     * @param index
     * @param matriculaFuncionario
     */
    public void concluirAgendamento(int index, String matriculaFuncionario) {
        if (index >= 0 && index < agenda.getAgendamentos().size()) {
            Agendamento agendamento = agenda.getAgendamentos().get(index);
            agendamento.setStatus("Concluído");
            
            // Registrar a transação no caixa
            String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String servicosStr = agendamento.getServicos().stream()
                                .map(Servico::getNome)
                                .collect(Collectors.joining(", "));
            String descricao = "Serviços: " + servicosStr + " - Cliente: " + agendamento.getCliente().getNome();
            
            caixa.registrarEntrada(agendamento.getValorTotal(), descricao, dataAtual, "Serviços", matriculaFuncionario,null);
            
            System.out.println("Agendamento concluído e pagamento registrado!");
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
        clientes.sort(Cliente.POR_NOME); // Ordena por nome antes de listar
        clientes.forEach(System.out::println);
    }

        /**
     * Registra a compra de peças no estoque como despesa no caixa.
     * 
     * @param codigo Código do item
     * @param nome Nome do item
     * @param quantidade Quantidade comprada
     * @param precoUnitario Preço unitário do item
     * @param data Data da compra
     */
    public void registrarCompraPecas(String codigo, String nome, int quantidade, double precoUnitario, String data) {
        double valorTotal = quantidade * precoUnitario;
        estoque.adicionarItem(codigo, nome, quantidade);
        caixa.registrarSaida(valorTotal, "Compra de peças: " + nome + " (" + quantidade + " un)", data,null,null,null);
    }

    /**
     * Registra o pagamento de salários como despesa no caixa.
     * 
     * @param data Data do pagamento
     */
    public void registrarPagamentoSalarios(String data) {
        double totalSalarios = funcionarios.stream()
                                        .mapToDouble(Funcionario::getSalario)
                                        .sum();
        caixa.registrarSaida(totalSalarios, "Pagamento de salários", data,null,null,null);
        System.out.println("Pagamento de salários registrado: R$" + totalSalarios);
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
    
    /***
     * Método que retorna o CPF anonimizado.
     * @return cpfAnonimizado
     */
    public String anonimizarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) return "CPF INVÁLIDO";
        return "***." + cpf.substring(3, 6) + ".***-" + cpf.substring(9, 11);
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
        caixa.registrarEntrada(valor, descricao, data,null,null,null);
    }

    /**
     * Registra uma despesa no caixa.
     *
     * @param valor Valor da despesa.
     * @param descricao Descrição da despesa.
     * @param data Data da despesa.
     */
    public void registrarDespesa(double valor, String descricao, String data) {
        caixa.registrarSaida(valor, descricao, data,null,null,null);
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
    /***
     * Define o caixa financeiro da oficina.
     */
    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }


    /***
     * Define o estoque da oficina.
     * @param estoque
     */
    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }


    /***
     * Define a lista de funcionários.
     * @param funcionarios Lista de funcionários a ser definida.
     */
    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }
    /***
     * Define a lista de clientes.
     * @param clientes Lista de clientes a ser definida.
     */
    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
    
    /**
     * Define a lista de serviços.
     * @param servicos
     */
    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }
    /***
     * Define a lista de serviços.
     * @param servicos Lista de serviços a ser definida.
     */
    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
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
    public void ordenarTudo(List<Cliente> clientes, List<Agendamento> agendamentos, List<Servico> servicos) {
        // 1. Ordenar clientes alfabeticamente
        if (clientes != null) {
            clientes.sort(Cliente.POR_NOME);
        }

        // 2. Ordenar agendamentos por data
        if (agendamentos != null) {
            agendamentos.sort(new Agenda.Agendamento.PorData());
        }

        // 3. Ordenar serviços por preço
        if (servicos != null) {
            servicos.sort(Comparator.comparingDouble(Servico::getValor));
        }
    }

}