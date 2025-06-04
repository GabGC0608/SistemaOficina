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
import java.time.LocalDate;
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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.DeserializationFeature;

/**
 * Classe que representa a oficina mecânica.
 * Gerencia elevadores, funcionários, clientes, serviços, agenda, estoque e caixa.
 */
public class Oficina {
    private Elevador[] elevadores; // Array de elevadores da oficina
    private List<Funcionario> funcionarios; // Lista de funcionários
    public List<Cliente> clientes; // Lista de clientes
    private List<Servico> servicos; // Lista de serviços disponíveis
    private Agenda agenda; // Agenda de serviços
    private Estoque estoque; // Estoque de itens
    private Caixa caixa; // Caixa financeiro da oficina
    private List<PontoFuncionario> registrosPonto; // Lista de registros de ponto dos funcionários
    private List<OrdemServico> ordensServico; // Lista de ordens de serviço
    
    private static Scanner scanner = new Scanner(System.in);
    private static Login loginManager = new Login(); // Inicializa com um objeto padrão
    protected static int contadorVeiculos = 0; // Contador de veículos (protected)

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
        this.ordensServico = new ArrayList<>();
        inicializarServicosPadrao();
    }

    /**
     * Inicializa a lista de serviços com serviços padrão.
     */
    public void inicializarServicosPadrao() {
        servicos.clear();
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
    /**
     * Registra a entrada ou saída de um funcionário no ponto.
     * @param isEntrada true para registrar entrada, false para registrar saída
     */
    public void registrarPonto(boolean isEntrada) {
        
        String matricula = lerString("Digite a matrícula: ");
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
        System.out.println(isEntrada ? "Entrada registrada!" : "Saída registrada!");
    }

    
    /**
     * Retorna a lista de registros de ponto dos funcionários.
     * @return Lista de registros de ponto dos funcionários
     */
    public List<PontoFuncionario> getRegistrosPonto() {
        return registrosPonto;
    }
    /**
     * Define a lista de registros de ponto dos funcionários.
     * @param registrosPonto Lista de registros de ponto a ser definida
     */
    public void setRegistrosPonto(List<PontoFuncionario> registrosPonto) {
        this.registrosPonto = registrosPonto;
    }
    /**
     * Define a lista de ordens de serviço.
     * @param ordensServico Lista de ordens de serviço a ser definida
     */
    public void setOrdensServico(List<OrdemServico> ordensServico) {
        this.ordensServico = ordensServico;
    }
    /**
     * Obtém a lista de ordens de serviço.
     * @return Lista de ordens de serviço
     */
    public List<OrdemServico> getOrdensServico() {
        return ordensServico;
    }
    


    // Métodos para gerenciar o estoque
    /**
     * Adiciona um novo item ao estoque da oficina.
     * Solicita ao usuário as informações do item a ser adicionado.
     */
    public void adicionarItemEstoque() {
        System.out.println("\n=== ADICIONAR ITEM ===");
        String codigo = lerString("Código: ");
        String nome = lerString("Nome: ");
        int quantidade = lerInteiro("Quantidade: ");
        double preco = lerDouble("Preço unitário: ");
        String descricao = lerString("Descrição: ");
        
        estoque.adicionarItem(codigo, nome, quantidade, preco, descricao);
    }

    /**
     * Remove um item do estoque da oficina.
     * Solicita ao usuário o código do item a ser removido.
     */
    public void removerItemEstoque() {
        estoque.listarItens();
        String codigo = lerString("Digite o código do item a remover: ");;

        estoque.removerItem(codigo);
        System.out.println("Item removido do estoque!");
    }

    /**
     * Atualiza a quantidade de um item no estoque.
     * Solicita ao usuário o código do item e a nova quantidade.
     */
    public void atualizarItemEstoque() {
        estoque.listarItens();
        String codigo = lerString("Digite o código do item: ");
        int quantidade = lerInteiro("Nova quantidade: ");
   
        
        estoque.atualizarQuantidade(codigo, quantidade);
        System.out.println("Quantidade atualizada!");
    }
    /**
     * Vende um item do estoque.
     * @param codigo Código do item a ser vendido
     * @param quantidade Quantidade do item a ser vendida
     */
    public void venderItemEstoque(String codigo, int quantidade) {
        estoque.venderItem(codigo, quantidade);
    }
    /**
     * Lista todos os itens disponíveis no estoque.
     */
    public void listarEstoque() {
        estoque.listarItens();
    }

    /**
     * Obtém o estoque da oficina.
     * @return Objeto Estoque que representa o estoque da oficina
     */
    public Estoque getEstoque() {
        return estoque;
    }

    // Métodos para gerenciar elevadores
    /**
     * Obtém um elevador disponível para uso.
     * @return Elevador disponível ou null se todos estiverem ocupados
     */
    public Elevador obterElevadorDisponivel() {
        for (Elevador elevador : elevadores) {
            if (elevador.getPeso() == 0) { // Elevador vazio
                return elevador;
            }
        }
        return null; // Nenhum elevador disponível
    }

    /**
     * Libera um elevador após o uso.
     * Solicita ao usuário o número do elevador a ser liberado.
     */
     public void liberarElevador() {
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
     * Agenda um novo serviço para um cliente.
     * Solicita todas as informações necessárias para o agendamento.
     */
    public void agendarServico() {
        System.out.println("\n=== REALIZAR AGENDAMENTO ===");

        // Listar clientes para seleção
        System.out.println("\nClientes disponíveis:");
        listarClientes();
        String clienteId = lerString("Digite o ID do cliente: ");
        Cliente cliente = getClientes().stream()
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
            List<Servico> servicos = getServicos();
            for (int i = 0; i < servicos.size(); i++) {
                System.out.println((i + 1) + ". " + servicos.get(i).getNome() + " - R$" + servicos.get(i).getValor());
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

        // Listar funcionários disponíveis para o serviço principal
        System.out.println("\nFuncionários disponíveis para o serviço:");
        listarFuncionarios();
        String matriculaFuncionario = lerString("Digite a matrícula do funcionário responsável: ");
        Funcionario funcionario = getFuncionarios().stream()
            .filter(f -> f.getMatricula() != null && f.getMatricula().equalsIgnoreCase(matriculaFuncionario))
            .findFirst()
            .orElse(null);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado!");
            return;
        }

        // Selecionar inspetor para a inspeção obrigatória
        System.out.println("\nFuncionários disponíveis para inspeção inicial:");
        listarFuncionarios();
        String matriculaInspetor = lerString("Digite a matrícula do inspetor responsável: ");
        Funcionario inspetor = getFuncionarios().stream()
            .filter(f -> f.getMatricula() != null && f.getMatricula().equalsIgnoreCase(matriculaInspetor))
            .findFirst()
            .orElse(null);

        if (inspetor == null) {
            System.out.println("Inspetor não encontrado!");
            return;
        }

        // Solicitar data e hora
        String data = lerData("Digite a data do agendamento (dd/MM/yyyy): ");
        String hora = lerHora("Digite a hora do agendamento (HH:mm): ");
        String dataHora = data + " " + hora;

        // Verificar disponibilidade do funcionário
        if (getAgenda().verificarDisponibilidadeFuncionario(funcionario, dataHora)) {
            System.out.println("Este funcionário já possui um agendamento neste horário!");
            return;
        }

        // Verificar disponibilidade do inspetor
        if (getAgenda().verificarDisponibilidadeFuncionario(inspetor, dataHora)) {
            System.out.println("Este inspetor já possui um agendamento neste horário!");
            return;
        }

        String status = "A fazer"; 

        // Criar o agendamento com inspeção obrigatória
        Agenda.Agendamento novoAgendamento = agenda.adicionarAgendamento(
            cliente, veiculo, servicosSelecionados, funcionario, dataHora, status, inspetor);

        if (novoAgendamento != null) {
            System.out.println("Agendamento realizado com sucesso!");
            System.out.println(novoAgendamento);
        } else {
            System.out.println("Erro ao realizar o agendamento!");
        }
    }


    /**
     * Cancela um agendamento existente.
     * Registra o valor retido (20% do valor total) no caixa como entrada.
     */
        public void cancelarAgendamento() {
         getAgenda().listarAgendamentos();

         if (getAgenda().getAgendamentos().isEmpty()) {
             System.out.println("Não há agendamentos para cancelar.");
             return;
         }

         int index = lerInteiro("Digite o número do agendamento a cancelar: ") - 1;

         if (index < 0 || index >= getAgenda().getAgendamentos().size()) {
             System.out.println("Número de agendamento inválido!");
             return;
         }

         Agenda.Agendamento ag = getAgenda().getAgendamentos().get(index);
         if ("Concluído".equalsIgnoreCase(ag.getStatus())) {
             System.out.println("Não é possível cancelar um agendamento já concluído!");
             return;
         }

         System.out.println("\nAgendamento selecionado:");
         System.out.println(ag);
         String confirmacao = lerString("Deseja realmente cancelar este agendamento? (S/N): ");

         String matricula = ""; // Declare matricula fora do if
         if (confirmacao.equalsIgnoreCase("S")) {
             matricula = lerString("Digite sua matrícula para confirmar: ");
             // Lógica de cancelamento AQUI, usando index e matricula
             agenda.getAgendamentos().remove(index);
             System.out.println("Agendamento cancelado!");

             if (index >= 0 && index < agenda.getAgendamentos().size()) {
                 Agenda.Agendamento agendamento = agenda.getAgendamentos().get(index);
                 double valorRetido = agendamento.getValorTotal() * 0.20;

                 // Registrar a transação no caixa
                 String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                 String descricao = "Cancelamento agendamento - Cliente: " + agendamento.getCliente().getNome();
                 caixa.registrarEntrada(valorRetido, descricao, dataAtual, "Cancelamentos", matricula, null);

                 System.out.println("Valor retido registrado!");
             }

         } else {
             System.out.println("Cancelamento não realizado.");
             return; // Importante: sair do método se não cancelar
         }
     }

public void concluirAgendamento() {
    getAgenda().listarAgendamentos();

    if (getAgenda().getAgendamentos().isEmpty()) {
        System.out.println("Não há agendamentos para concluir.");
        return;
    }

    int index = lerInteiro("Digite o número do agendamento a concluir: ") - 1;

    if (index < 0 || index >= getAgenda().getAgendamentos().size()) {
        System.out.println("Número de agendamento inválido!");
        return;
    }

    Agenda.Agendamento ag = getAgenda().getAgendamentos().get(index);

    if ("Concluído".equalsIgnoreCase(ag.getStatus())) {
        System.out.println("Este agendamento já foi concluído!");
        return;
    }

    if ("Cancelado".equalsIgnoreCase(ag.getStatus())) {
        System.out.println("Não é possível concluir um agendamento cancelado!");
        return;
    }

    System.out.println("\nAgendamento selecionado:");
    System.out.println(ag);
    String confirmacao = lerString("Deseja realmente concluir este agendamento? (S/N): ");

    String matricula = ""; // Declare matricula fora do if
    if (confirmacao.equalsIgnoreCase("S")) {
        matricula = lerString("Digite sua matrícula para confirmar: ");
        // Lógica de conclusão AQUI, usando index e matricula
        agenda.getAgendamentos().get(index).setStatus("Concluído");
        System.out.println("Agendamento concluído!");

        if (index >= 0 && index < agenda.getAgendamentos().size()) {
            Agenda.Agendamento agendamento = agenda.getAgendamentos().get(index);

            // Registrar a transação no caixa
            String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String servicosStr = agendamento.getServicos().stream()
                    .map(Servico::getNome)
                    .collect(Collectors.joining(", "));
            String descricao = "Serviços: " + servicosStr + " - Cliente: " + agendamento.getCliente().getNome();

            caixa.registrarEntrada(agendamento.getValorTotal(), descricao, dataAtual, "Serviços", matricula, null);

            System.out.println("Pagamento registrado!");
        }
    } else {
        System.out.println("Conclusão não realizada.");
        return;
    }
}

    /**
     * Adiciona um novo veículo a um cliente existente.
     * Solicita ao usuário as informações do veículo.
     */
    public void adicionarVeiculoACliente() {
        String telefone = lerString("Digite o telefone do cliente: ");
        Cliente cliente = getClientes().stream()
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
     * Obtém o contador total de veículos cadastrados.
     * @return Número total de veículos cadastrados na oficina
     */
    public int getContadorVeiculos() {
        return contadorVeiculos;
    }

     /**
     * Lista todos os veículos cadastrados na oficina.
     * Exibe os veículos agrupados por cliente.
     */
    public void listarVeiculos() {
        System.out.println("\n=== TODOS OS VEÍCULOS ===");
        boolean encontrouVeiculos = false;
        
        for (Cliente cliente : getClientes()) {
            for (Veiculo veiculo : cliente.getVeiculos()) {
                System.out.println("Cliente: " + cliente.getNome());
                System.out.println(veiculo);
                System.out.println();
                encontrouVeiculos = true;
            }
        }
        
        if (!encontrouVeiculos) {
            System.out.println("Nenhum veículo cadastrado.");
        }
    }

    /**
     * Busca um veículo pelo número da placa.
     * @param placa Placa do veículo a ser buscado
     * @return Veículo encontrado ou null se não encontrado
     */
    public Veiculo buscarVeiculoPorPlaca(String placa) {
        return getClientes().stream()
            .flatMap(cliente -> cliente.getVeiculos().stream())
            .filter(v -> ((Veiculo)v).getPlaca().equalsIgnoreCase(placa))
            .findFirst()
            .orElse(null);
    }

    // Métodos para gerenciar funcionários
    /**
     * Contrata um novo funcionário.
     * Solicita todas as informações necessárias para a contratação.
     */
    public void contratarFuncionario() {
        System.out.println("\n=== CADASTRAR FUNCIONÁRIO ===");
        String nome = lerString("Nome: ");
        String telefone = lerString("Telefone: ");
        String endereco = lerString("Endereço: ");
        String cargo = lerString("Cargo: ");
        double salario = lerDouble("Salário: ");
        String matricula = lerString("Matrícula: ");
        
        if (cargo.equalsIgnoreCase("Administrador")) {
            Administrador admin = new Administrador(nome, telefone, endereco, salario, matricula);
            funcionarios.add(admin);
            System.out.println("Administrador " + admin.getNome() + " contratado com sucesso!");
        } else {
            String especialidade = lerString("Especialidade (Motor/Elétrica/Suspensão/etc): ");
            Funcionario funcionario = new Funcionario(nome, telefone, endereco, cargo, salario, matricula, especialidade);
            funcionarios.add(funcionario);
            System.out.println("Funcionário " + funcionario.getNome() + " contratado com sucesso!");
        }
    }

    public void contratarFuncionario(Funcionario funcionario) {
        if (funcionario != null) {
            funcionarios.add(funcionario);
            System.out.println("Funcionário " + funcionario.getNome() + " contratado com sucesso!");
        }
    }

    /**
     * Demite um funcionário existente.
     * Solicita a matrícula do funcionário a ser demitido.
     */
    public void demitirFuncionario() {
        String matricula = lerString("Digite a matrícula do funcionário a ser removido: ");
        funcionarios.removeIf(f -> f.getMatricula().equals(matricula));
        System.out.println("Funcionário com matrícula " + matricula + " demitido com sucesso!");
    }

    /**
     * Lista todos os funcionários da oficina.
     */
    public void listarFuncionarios() {
        System.out.println("\n=== FUNCIONÁRIOS ===");
        funcionarios.forEach(System.out::println);
    }

    /**
     * Busca um funcionário pelo nome.
     * @param nome Nome do funcionário a ser buscado
     * @return Funcionário encontrado ou null se não encontrado
     */
    public Funcionario buscarFuncionarioPorNome(String nome) {
        return getFuncionarios().stream()
            .filter(f -> f.getNome().equalsIgnoreCase(nome))
            .findFirst()
            .orElse(null);
    }
     /**
     * Mostra o registro de ponto de todos os funcionários.
     */
    public void mostrarRegistroPonto() {
        System.out.println("\n=== REGISTROS DE PONTO ===");
        for (PontoFuncionario ponto : getRegistrosPonto()) {
            System.out.println(ponto.toString());
        }
    }

    /**
     * Altera os dados de um funcionário existente.
     * Permite modificar nome, telefone, endereço, cargo e salário.
     */
    public void alterarDadosFuncionario() {
        System.out.println("\n=== ALTERAR DADOS DO FUNCIONÁRIO ===");
        String matricula = lerString("Digite a matrícula do funcionário: ");
        
        // Busca o funcionário pela matrícula
        Funcionario funcionario = getFuncionarios().stream()
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
     * Altera a senha do usuário.
     */
    public void alterarSenha() {
        System.out.println("\n=== ALTERAR SENHA ===");
        String usuario = lerString("Digite o nome de usuário: ");
        String senhaAtual = lerString("Digite a senha atual: ");
        String novaSenha = lerString("Digite a nova senha: ");
        
        // Chama o método da classe Login para alterar a senha
        loginManager.alterarSenha(usuario, senhaAtual, novaSenha);
    }

    // Métodos para gerenciar clientes
    /**
     * Cadastra um novo cliente.
     *
     * @param cliente Cliente a ser cadastrado.
     */
    public void cadastrarCliente() {
        System.out.println("\n=== CADASTRAR CLIENTE ===");
        String nome = lerString("Nome: ");
        String telefone = lerString("Telefone: ");
        String endereco = lerString("Endereço: ");
        String cpf = lerString("CPF (11 dígitos): ");
        String cpfAnonimizado = anonimizarCPF(cpf);
        
        Cliente cliente = new Cliente(nome, telefone, endereco, cpfAnonimizado);
        clientes.add(cliente);
        System.out.println("Cliente " + cliente.getNome() + " cadastrado com sucesso!");
    }

    /**
     * Remove um cliente existente.
     * Solicita o telefone do cliente a ser removido.
     */
    public void removerCliente() {
        String telefone = lerString("Digite o telefone do cliente a ser removido: ");
        clientes.removeIf(c -> c.getTelefone().equals(telefone));
        System.out.println("Cliente com telefone " + telefone + " removido com sucesso!");
    }

    /**
     * Lista todos os clientes cadastrados.
     */
    public void listarClientes() {
        System.out.println("\n=== CLIENTES ===");
        for (Cliente cliente : getClientes()) {
            System.out.println(cliente.toString());
        }
    }
    /**
     * Busca um cliente pelo nome.
     * Exibe todos os clientes que correspondem ao nome buscado.
     */
    public void buscarClientePorNome() {
        String nome = lerString("Digite o nome do cliente: ");
        getClientes().stream()
            .filter(c -> c.getNome().equalsIgnoreCase(nome))
            .forEach(System.out::println);
    }

   /**
    * Altera os dados de um cliente existente.
    * Permite modificar nome, telefone, endereço e ID do cliente.
    */
    public void alterarDadosCliente() {
        System.out.println("\n=== ALTERAR DADOS DO CLIENTE ===");
        String telefone = lerString("Digite o telefone do cliente: ");
        
        // Busca o cliente pelo telefone
        Cliente cliente = getClientes().stream()
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
     * Registra a compra de peças no estoque.
     * Adiciona as peças ao estoque e registra a despesa no caixa.
     */
    public void registrarCompraPecas() {
        System.out.println("\n=== REGISTRAR COMPRA DE PEÇAS ===");
        String codigo = lerString("Código da peça: ");
        String nome = lerString("Nome da peça: ");
        int quantidade = lerInteiro("Quantidade: ");
        double precoUnitario = lerDouble("Preço unitário: ");
        String data = lerString("Data da compra (dd/MM/yyyy): ");
        String responsavel = lerString("Responsável: ");
        
        double valorTotal = quantidade * precoUnitario;
        estoque.adicionarItem(codigo, nome, quantidade);
        caixa.registrarSaida(valorTotal, "Compra de peças: " + nome + " (" + quantidade + " un)", 
                            data, "Compra de Peças", responsavel, null);
    }

    /**
     * Registra o pagamento de salários dos funcionários.
     * Calcula o total de salários e registra como despesa no caixa.
     */
    public void registrarPagamentoSalarios() {
        String data = lerString("Digite a data do pagamento (dd/MM/yyyy): ");
        String responsavel = lerString("Responsável: ");
        
        double totalSalarios = funcionarios.stream()
                                        .mapToDouble(Funcionario::getSalario)
                                        .sum();
        caixa.registrarSaida(totalSalarios, "Pagamento de salários", data, "Salários", responsavel, null);
        System.out.println("Pagamento de salários registrado: R$" + totalSalarios);
    }

    // Métodos para gerenciar serviços
    /**
     * Adiciona um serviço à lista de serviços.
     *
     * @param servico Serviço a ser adicionado.
     */
    public void adicionarServico() {
        System.out.println("\n=== CADASTRAR SERVIÇO ===");
        String nome = lerString("Nome: ");
        String descricao = lerString("Descrição: ");
        double valor = lerDouble("Valor: ");
        int tempo = lerInteiro("Tempo estimado (minutos): ");
        
        Servico novoServico = new Servico(nome, descricao, valor, tempo);
        
        servicos.add(novoServico);
        System.out.println("Serviço " + novoServico.getNome() + " adicionado com sucesso!");
    }

        /**
     * Remove um serviço da lista de serviços disponíveis.
     * Solicita o índice do serviço a ser removido.
     */
    public void removerServico() {
        listarServicos();
        int index = lerInteiro("Digite o número do serviço a ser removido: ");
        
        if (index >= 1 && index <= servicos.size()) {
            Servico servicoRemovido = servicos.remove(index - 1);
            System.out.println("Serviço " + servicoRemovido.getNome() + " removido com sucesso!");
        } else {
            System.out.println("Número de serviço inválido!");
        }
    }

    /**
     * Lista todos os serviços disponíveis na oficina.
     */
    public void listarServicos() {
        System.out.println("\n=== SERVIÇOS DISPONÍVEIS ===");
        for (int i = 0; i < servicos.size(); i++) {
            System.out.println((i + 1) + ". " + servicos.get(i));
        }
    }
    /**
     * Lista todos os agendamentos registrados.
     */
    public void listarAgendamentos() {
        agenda.listarAgendamentos();
    }
    /**
     * Lista os agendamentos de um cliente específico.
     * Solicita o nome do cliente para filtrar os agendamentos.
     */
    public void listarAgendamentosPorCliente() {
        String nome = lerString("Digite o nome do cliente: ");
        getAgenda().listarAgendamentosPorCliente(nome);
    }
    /**
     * Lista os agendamentos por status.
     * Filtra os agendamentos por: Agendado, Concluído ou Cancelado.
     */
    public void listarAgendamentosPorStatus() {
        String status = lerString("Digite o status (Agendado/Concluído/Cancelado): ");
        getAgenda().listarAgendamentosPorStatus(status);
    }
    
    /**
     * Anonimiza um CPF para proteger os dados do cliente.
     * @param cpf CPF a ser anonimizado
     * @return CPF anonimizado no formato "***.***.***-**"
     */
    public String anonimizarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) return "CPF INVÁLIDO";
        return "***." + cpf.substring(3, 6) + ".***-" + cpf.substring(9, 11);
    }
        

    // Métodos para gerenciar finanças
    /**
     * Registra um pagamento no caixa.
     * Solicita valor, descrição, data e responsável pelo pagamento.
     */
    public void registrarPagamento() {
        double valor = lerDouble("Valor: ");
        String descricao = lerString("Descrição: ");
        String data = lerString("Data (dd/MM/yyyy): ");
        String responsavel = lerString("Responsável: ");

        caixa.registrarEntrada(valor, descricao, data, "Pagamento", responsavel, null);
    }

    /**
     * Registra uma despesa no caixa.
     * Solicita valor, descrição, data e responsável pela despesa.
     */
    public void registrarDespesa() {
        double valor = lerDouble("Valor: ");
        String descricao = lerString("Descrição: ");
        String data = lerString("Data (dd/MM/yyyy): ");
        String responsavel = lerString("Responsável: ");
        
        caixa.registrarSaida(valor, descricao, data, "Despesa", responsavel, null);
    }

    /**
     * Gera um relatório das transações financeiras de um dia específico.
     * Solicita a data para geração do relatório.
     */
    public void gerarRelatorioDiario() {
        String data = lerString("Digite a data (dd/MM/yyyy): ");
        
        if (data == null || !data.matches("\\d{2}/\\d{2}/\\d{4}")) {
            System.out.println("Formato de data inválido! Use dd/MM/yyyy (ex: 15/05/2025)");
            return;
        }
       
        caixa.gerarRelatorioDiario(data);
    }

    /**
     * Gera um relatório das transações financeiras de um mês específico.
     * Solicita o mês e ano para geração do relatório.
     */
    public void gerarRelatorioMensal() {
        int mes = lerInteiro("Digite o mês (1-12): ");
        int ano = lerInteiro("Digite o ano: ");
        
        caixa.gerarRelatorioMensal(mes, ano);
    }

    public void listarTodasTransacoes() {
        System.out.println("\n=== TODAS AS ORDENS DE SERVIÇO ===");
        List<OrdemServico> ordens = caixa.getOrdensServico();
        if (ordens.isEmpty()) {
            System.out.println("Nenhuma ordem de serviço encontrada.");
            return;
        }
        ordens.forEach(System.out::println);
    }

    public void listarTransacoesPorPeriodo() {
        System.out.println("\n=== FILTRAR ORDENS DE SERVIÇO POR PERÍODO ===");
        String dataInicio = lerData("Digite a data inicial (dd/mm/aaaa): ");
        String dataFim = lerData("Digite a data final (dd/mm/aaaa): ");
        
        List<OrdemServico> ordens = caixa.getOrdensPorPeriodo(dataInicio, dataFim);
        if (ordens.isEmpty()) {
            System.out.println("Nenhuma ordem de serviço encontrada no período especificado.");
            return;
        }
        
        double totalEntradas = 0;
        double totalSaidas = 0;
        
        System.out.println("\nOrdens de serviço no período de " + dataInicio + " a " + dataFim + ":");
        for (OrdemServico ordem : ordens) {
            System.out.println(ordem);
            if (ordem.getTipo().equals("Entrada")) {
                totalEntradas += ordem.getValor();
            } else {
                totalSaidas += ordem.getValor();
            }
        }
        
        System.out.println("\nResumo do período:");
        System.out.printf("Total de entradas: R$ %.2f%n", totalEntradas);
        System.out.printf("Total de saídas: R$ %.2f%n", totalSaidas);
        System.out.printf("Saldo do período: R$ %.2f%n", (totalEntradas - totalSaidas));
    }

    public void listarTransacoesPorCategoria() {
        System.out.println("\n=== FILTRAR ORDENS DE SERVIÇO POR CATEGORIA ===");
        System.out.println("Categorias disponíveis:");
        System.out.println("1. Serviços");
        System.out.println("2. Peças");
        System.out.println("3. Salários");
        System.out.println("4. Outros");
        
        int opcao = lerInteiro("Digite o número da categoria: ");
        String categoria;
        
        switch (opcao) {
            case 1:
                categoria = "Serviços";
                break;
            case 2:
                categoria = "Peças";
                break;
            case 3:
                categoria = "Salários";
                break;
            case 4:
                categoria = "Outros";
                break;
            default:
                System.out.println("Opção inválida!");
                return;
        }
        
        List<OrdemServico> ordens = caixa.getOrdensPorCategoria(categoria);
        if (ordens.isEmpty()) {
            System.out.println("Nenhuma ordem de serviço encontrada na categoria " + categoria);
            return;
        }
        
        double totalEntradas = 0;
        double totalSaidas = 0;
        
        System.out.println("\nOrdens de serviço da categoria " + categoria + ":");
        for (OrdemServico ordem : ordens) {
            System.out.println(ordem);
            if (ordem.getTipo().equals("Entrada")) {
                totalEntradas += ordem.getValor();
            } else {
                totalSaidas += ordem.getValor();
            }
        }
        
        System.out.println("\nResumo da categoria " + categoria + ":");
        System.out.printf("Total de entradas: R$ %.2f%n", totalEntradas);
        System.out.printf("Total de saídas: R$ %.2f%n", totalSaidas);
        System.out.printf("Saldo da categoria: R$ %.2f%n", (totalEntradas - totalSaidas));
    }

    public void listarTransacoesPorTipo() {
        System.out.println("\n=== FILTRAR ORDENS DE SERVIÇO POR TIPO ===");
        System.out.println("1. Entradas");
        System.out.println("2. Saídas");
        
        int opcao = lerInteiro("Digite o número do tipo: ");
        String tipo = (opcao == 1) ? "Entrada" : "Saída";
        
        if (opcao != 1 && opcao != 2) {
            System.out.println("Opção inválida!");
            return;
        }
        
        List<OrdemServico> ordens = caixa.getOrdensServico().stream()
            .filter(t -> t.getTipo().equals(tipo))
            .collect(Collectors.toList());
        
        if (ordens.isEmpty()) {
            System.out.println("Nenhuma ordem de serviço do tipo " + tipo.toLowerCase() + " encontrada.");
            return;
        }
        
        double total = 0;
        System.out.println("\nOrdens de serviço do tipo " + tipo.toLowerCase() + ":");
        for (OrdemServico ordem : ordens) {
            System.out.println(ordem);
            total += ordem.getValor();
        }
        
        System.out.println("\nTotal de " + tipo.toLowerCase() + "s: R$ " + total);
    }

    public void listarTransacoesPorResponsavel() {
        System.out.println("\n=== FILTRAR ORDENS DE SERVIÇO POR RESPONSÁVEL ===");
        String responsavel = lerString("Digite o nome do responsável: ");
        
        List<OrdemServico> ordens = caixa.getOrdensServico().stream()
            .filter(t -> t.getResponsavel() != null && t.getResponsavel().equalsIgnoreCase(responsavel))
            .collect(Collectors.toList());
        
        if (ordens.isEmpty()) {
            System.out.println("Nenhuma ordem de serviço encontrada para o responsável " + responsavel);
            return;
        }
        
        double totalEntradas = 0;
        double totalSaidas = 0;
        
        System.out.println("\nOrdens de serviço do responsável " + responsavel + ":");
        for (OrdemServico ordem : ordens) {
            System.out.println(ordem);
            if (ordem.getTipo().equals("Entrada")) {
                totalEntradas += ordem.getValor();
            } else {
                totalSaidas += ordem.getValor();
            }
        }
        
        System.out.println("\nResumo do responsável " + responsavel + ":");
        System.out.printf("Total de entradas: R$ %.2f%n", totalEntradas);
        System.out.printf("Total de saídas: R$ %.2f%n", totalSaidas);
        System.out.printf("Saldo do responsável: R$ %.2f%n", (totalEntradas - totalSaidas));
    }

    /**
     * Gera relatório estatístico detalhado do período especificado
     * Inclui informações como mecânico mais requisitado, serviços mais populares,
     * faturamento total, média de serviços por dia, etc.
     */
    public void gerarRelatorioEstatistico() {
        System.out.println("\n=== RELATÓRIO ESTATÍSTICO DETALHADO ===");
        
        // Solicita período para análise
        String dataInicio = lerData("Digite a data inicial (dd/mm/aaaa): ");
        String dataFim = lerData("Digite a data final (dd/mm/aaaa): ");
        
        List<OrdemServico> ordens = caixa.getOrdensPorPeriodo(dataInicio, dataFim);
        if (ordens.isEmpty()) {
            System.out.println("Nenhuma ordem de serviço encontrada no período especificado.");
            return;
        }

        // Mapas para contabilizar estatísticas
        Map<String, Integer> servicosPorMecanico = new HashMap<>();
        Map<String, Integer> servicosMaisRealizados = new HashMap<>();
        Map<String, Double> faturamentoPorMecanico = new HashMap<>();
        double faturamentoTotal = 0;
        int totalServicos = 0;

        // Processa cada ordem de serviço
        for (OrdemServico ordem : ordens) {
            if (ordem.getTipo().equals("Entrada")) {
                // Contabiliza serviços por mecânico
                String mecanico = ordem.getResponsavel();
                servicosPorMecanico.merge(mecanico, 1, Integer::sum);
                faturamentoPorMecanico.merge(mecanico, ordem.getValor(), Double::sum);

                // Contabiliza serviços mais realizados
                String servico = ordem.getDescricao();
                servicosMaisRealizados.merge(servico, 1, Integer::sum);

                faturamentoTotal += ordem.getValor();
                totalServicos++;
            }
        }

        // Exibe estatísticas
        System.out.println("\n=== PERÍODO: " + dataInicio + " a " + dataFim + " ===");
        
        // 1. Mecânicos mais requisitados
        System.out.println("\n=== TOP MECÂNICOS ===");
        servicosPorMecanico.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(3)
            .forEach(e -> System.out.printf("%s: %d serviços%n", e.getKey(), e.getValue()));

        // 2. Serviços mais realizados
        System.out.println("\n=== SERVIÇOS MAIS REALIZADOS ===");
        servicosMaisRealizados.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(5)
            .forEach(e -> System.out.printf("%s: %d vezes%n", e.getKey(), e.getValue()));

        // 3. Faturamento por mecânico
        System.out.println("\n=== FATURAMENTO POR MECÂNICO ===");
        faturamentoPorMecanico.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(e -> System.out.printf("%s: R$ %.2f%n", e.getKey(), e.getValue()));

        // 4. Estatísticas gerais
        System.out.println("\n=== ESTATÍSTICAS GERAIS ===");
        System.out.printf("Faturamento Total: R$ %.2f%n", faturamentoTotal);
        System.out.printf("Total de Serviços: %d%n", totalServicos);
        System.out.printf("Ticket Médio: R$ %.2f%n", faturamentoTotal / totalServicos);
        
        // 5. Análise de Estoque
        System.out.println("\n=== ANÁLISE DE ESTOQUE ===");
        Map<String, Integer> pecasMaisVendidas = new HashMap<>();
        for (OrdemServico ordem : ordens) {
            if (ordem.getTipo().equals("Saída") && ordem.getCategoria().equals("Peças")) {
                String peca = ordem.getDescricao();
                pecasMaisVendidas.merge(peca, 1, Integer::sum);
            }
        }
        
        System.out.println("Peças mais vendidas:");
        pecasMaisVendidas.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(5)
            .forEach(e -> System.out.printf("%s: %d unidades%n", e.getKey(), e.getValue()));

        // 6. Análise de Ocupação
        System.out.println("\n=== ANÁLISE DE OCUPAÇÃO ===");
        long diasPeriodo = ordens.stream()
            .map(o -> o.getData().split("/")[0]) // Pega só o dia
            .distinct()
            .count();
        
        System.out.printf("Média de serviços por dia: %.1f%n", (double) totalServicos / diasPeriodo);
        System.out.printf("Faturamento médio diário: R$ %.2f%n", faturamentoTotal / diasPeriodo);
    }

    /**
     * Gera relatório de desempenho dos mecânicos
     */
    public void gerarRelatorioDesempenhoMecanicos() {
        System.out.println("\n=== RELATÓRIO DE DESEMPENHO DOS MECÂNICOS ===");
        
        // Solicita período para análise
        String dataInicio = lerData("Digite a data inicial (dd/mm/aaaa): ");
        String dataFim = lerData("Digite a data final (dd/mm/aaaa): ");
        
        List<OrdemServico> ordens = caixa.getOrdensPorPeriodo(dataInicio, dataFim);
        if (ordens.isEmpty()) {
            System.out.println("Nenhuma ordem de serviço encontrada no período especificado.");
            return;
        }

        // Mapas para estatísticas por mecânico
        Map<String, Integer> totalServicos = new HashMap<>();
        Map<String, Double> totalFaturamento = new HashMap<>();
        Map<String, List<Double>> tempoServicos = new HashMap<>();

        // Processa cada ordem de serviço
        for (OrdemServico ordem : ordens) {
            if (ordem.getTipo().equals("Entrada")) {
                String mecanico = ordem.getResponsavel();
                
                // Contabiliza serviços e faturamento
                totalServicos.merge(mecanico, 1, Integer::sum);
                totalFaturamento.merge(mecanico, ordem.getValor(), Double::sum);
                
                // Adiciona tempo do serviço para cálculo de média
                tempoServicos.computeIfAbsent(mecanico, k -> new ArrayList<>())
                    .add(ordem.getValor()); // Assumindo que temos o tempo do serviço
            }
        }

        // Exibe estatísticas por mecânico
        System.out.println("\n=== DESEMPENHO INDIVIDUAL ===");
        totalServicos.keySet().forEach(mecanico -> {
            System.out.println("\nMecânico: " + mecanico);
            System.out.printf("Total de Serviços: %d%n", totalServicos.get(mecanico));
            System.out.printf("Faturamento Total: R$ %.2f%n", totalFaturamento.get(mecanico));
            
            // Calcula média de valor por serviço
            double mediaValor = totalFaturamento.get(mecanico) / totalServicos.get(mecanico);
            System.out.printf("Média de Valor por Serviço: R$ %.2f%n", mediaValor);
            
            // Calcula produtividade (serviços por dia)
            double servicosPorDia = (double) totalServicos.get(mecanico) / 
                ordens.stream().map(o -> o.getData()).distinct().count();
            System.out.printf("Média de Serviços por Dia: %.1f%n", servicosPorDia);
        });

        // Ranking de produtividade
        System.out.println("\n=== RANKING DE PRODUTIVIDADE ===");
        totalServicos.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(e -> System.out.printf("%s: %d serviços%n", e.getKey(), e.getValue()));

        // Ranking de faturamento
        System.out.println("\n=== RANKING DE FATURAMENTO ===");
        totalFaturamento.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(e -> System.out.printf("%s: R$ %.2f%n", e.getKey(), e.getValue()));
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
     * @param caixa Novo objeto Caixa a ser definido
     */
    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }


    /***
     * Define o estoque da oficina.
     * @param estoque Novo objeto Estoque a ser definido
     */
    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }


    /**
     * Define a lista de serviços.
     * @param servicos Lista de serviços a ser definida
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
    // Métodos auxiliares para leitura de dados
    /**
     * Lê uma data do console no formato dd/MM/yyyy.
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return Data no formato dd/MM/yyyy
     */
    public static String lerData(String mensagem) {
        while (true) {
            String input = lerString(mensagem);
            if (input.matches("\\d{2}/\\d{2}/\\d{4}")) {
                return input;
            }
            System.out.println("Formato inválido! Use dd/MM/yyyy (ex: 15/05/2025)");
        }
    }

    /**
     * Lê uma hora do console no formato HH:mm.
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return Hora no formato HH:mm
     */
    public static String lerHora(String mensagem) {
        while (true) {
            String input = lerString(mensagem);
            if (input.matches("\\d{2}:\\d{2}")) {
                return input;
            }
            System.out.println("Formato inválido! Use HH:mm (ex: 14:30)");
        }
    }

    /**
     * Lê uma string do console.
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return String lida do console
     */
    public static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }
    /**
     * Lê um número inteiro do console.
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return Número inteiro lido do console
     */
    public static int lerInteiro(String mensagem) {
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
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return Número decimal lido do console
     */
    public static double lerDouble(String mensagem) {
        while (true) {
            try {
                System.out.print(mensagem);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um valor válido!");
            }
        }
    }

    public void ordenarTudo(List<Cliente> clientes, List<Agenda.Agendamento> agendamentos, List<Servico> servicos) {
        // 1. Ordenar clientes alfabeticamente
        if (clientes != null) {
            clientes.sort(new Comparadores.ClientePorNome());
        }

        // 2. Ordenar agendamentos por data
        if (agendamentos != null) {
            agendamentos.sort(new Comparadores.AgendamentoPorData());
        }

        // 3. Ordenar serviços por preço
        if (servicos != null) {
            servicos.sort(new Comparadores.ServicoPorValor());
        }
    }

      /**
 * Salva os dados de todas as classes em arquivos JSON separados.
 * @throws IOException Se ocorrer um erro durante a escrita nos arquivos.
 */
public void salvarDados() throws IOException {
    // Criar diretório se não existir
    File dataDir = new File("data");
    if (!dataDir.exists()) {
        dataDir.mkdir();
    }

    // Salvar dados em arquivos JSON
    JsonUtil.salvarParaJson(getOrdensServico(), "data/transacoes.json");
    JsonUtil.salvarParaJson(getRegistrosPonto(), "data/pontos.json");
    JsonUtil.salvarParaJson(loginManager, "data/login.json");
    JsonUtil.salvarParaJson(contadorVeiculos, "data/contadorVeiculos.json");
    JsonUtil.salvarParaJson(getClientes(), "data/clientes.json");
    JsonUtil.salvarParaJson(getFuncionarios(), "data/funcionarios.json");
    JsonUtil.salvarParaJson(getServicos(), "data/servicos.json");
    JsonUtil.salvarParaJson(getEstoque(), "data/estoque.json");
    JsonUtil.salvarParaJson(getAgenda(), "data/agenda.json");
    JsonUtil.salvarParaJson(getCaixa(), "data/caixa.json");
}
  /**
 * Carrega os dados de todas as classes a partir de arquivos JSON separados.
 * @throws IOException Se ocorrer um erro durante a leitura dos arquivos.
 * @throws ClassNotFoundException Se ocorrer um erro na desserialização dos objetos.
 */
public void carregarDados() throws IOException, ClassNotFoundException {
    File arquivoOficina = new File("data/oficina.json");
    File arquivoLogin = new File("data/login.json");
    File arquivoClientes = new File("data/clientes.json");
    File arquivoFuncionarios = new File("data/funcionarios.json");
    File arquivoServicos = new File("data/servicos.json");
    File arquivoEstoque = new File("data/estoque.json");
    File arquivoAgenda = new File("data/agenda.json");
    File arquivoCaixa = new File("data/caixa.json");
    File arquivoPonto = new File("data/pontos.json");
    File arquivoTransacoes = new File("data/transacoes.json");

    ObjectMapper mapper = new ObjectMapper();



    if(arquivoPonto.exists()){
        registrosPonto = mapper.readValue(arquivoPonto, new TypeReference<List<PontoFuncionario>>() {});
        System.out.println("Dados dos pontos carregados com sucesso!");
    } else {
        registrosPonto = new ArrayList<>();
    }
    
    if(arquivoTransacoes.exists()){
        ordensServico = mapper.readValue(arquivoTransacoes, new TypeReference<List<OrdemServico>>() {});
        System.out.println("Dados das transações carregados com sucesso!");
    } else {
        ordensServico = new ArrayList<>();
    }

    // Carrega os dados da oficina
    //oficina = mapper.readValue(arquivoOficina, Oficina.class);
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
        setClientes(clientes);
        System.out.println("Dados dos clientes carregados com sucesso!");
    } else {
        setClientes(new ArrayList<>());
    }

    // Carrega os dados de funcionários
    if (arquivoFuncionarios.exists()) {
        List<Funcionario> funcionarios = mapper.readValue(arquivoFuncionarios, new TypeReference<List<Funcionario>>() {});
        setFuncionarios(funcionarios);
        System.out.println("Dados dos funcionários carregados com sucesso!");
    } else {
        setFuncionarios(new ArrayList<>());
    }

    // Carrega os dados de serviços
    if (arquivoServicos.exists()) {
        List<Servico> servicos = mapper.readValue(arquivoServicos, new TypeReference<List<Servico>>() {});
       setServicos(servicos);
        System.out.println("Dados dos serviços carregados com sucesso!");
    } else {
        setServicos(new ArrayList<>());
    }

    // Carrega os dados do estoque
    if (arquivoEstoque.exists()) {
        Estoque estoque = mapper.readValue(arquivoEstoque, Estoque.class);
        setEstoque(estoque);
        System.out.println("Dados do estoque carregados com sucesso!");
    } else {
        setEstoque(new Estoque());
    }

    // Carrega os dados da agenda
    if (arquivoAgenda.exists()) {
        Agenda agenda = mapper.readValue(arquivoAgenda, Agenda.class);
        setAgenda(agenda);
        System.out.println("Dados da agenda carregados com sucesso!");
    } else {
        setAgenda(new Agenda());
    }

    // Carrega os dados do caixa
    if (arquivoCaixa.exists()) {
        Caixa caixa = mapper.readValue(arquivoCaixa, Caixa.class);
        setCaixa(caixa);
        System.out.println("Dados do caixa carregados com sucesso!");
    } else {
        setCaixa(new Caixa());
    }
}


public void venderPecas() {
    // Lista todas as peças disponíveis no estoque
    System.out.println("\nPeças disponíveis no estoque:");
    estoque.listarItens();
    
    // Cria uma nova transação para venda de peças
    OrdemServico transacao = new OrdemServico();
    transacao.setTipo("Entrada");
    transacao.setDescricao("Venda de peças");
    transacao.setData(Oficina.lerData("Digite a data (dd/MM/yyyy): "));
    transacao.setCategoria("Peças");
    transacao.setResponsavel(Oficina.lerString("Digite o nome do vendedor: "));
    transacao.setCliente(Oficina.lerString("Digite o nome do cliente: "));
    
    boolean continuarVendendo = true;
    while (continuarVendendo) {
        String codigo = Oficina.lerString("Digite o código da peça a ser vendida (ou 0 para finalizar): ");
        
        if (codigo.equals("0")) {
            break;
        }
        
        // Encontra a peça no estoque
        Estoque.ItemEstoque peca = null;
        for (Estoque.ItemEstoque item : estoque.getItens()) {
            if (item.getCodigo().equals(codigo)) {
                peca = item;
                break;
            }
        }
        
        if (peca == null) {
            System.out.println("Peça não encontrada no estoque!");
            continue;
        }
        
        int quantidade = Oficina.lerInteiro("Digite a quantidade desejada: ");
        
        if (quantidade <= 0) {
            System.out.println("Quantidade inválida!");
            continue;
        }
        
        if (quantidade > peca.getQuantidade()) {
            System.out.println("Quantidade insuficiente no estoque! Disponível: " + peca.getQuantidade());
            continue;
        }
        
        // Adiciona a peça à transação e atualiza o estoque
        try {
            transacao.adicionarItemEstoque(peca, quantidade);
            peca.setQuantidade(peca.getQuantidade() - quantidade);
            System.out.println("Peça adicionada com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao adicionar peça: " + e.getMessage());
            continue;
        }
        
        String resposta = Oficina.lerString("Deseja adicionar mais peças? (S/N): ");
        continuarVendendo = resposta.equalsIgnoreCase("S");
    }
    
    // Adiciona a transação ao caixa apenas se houver itens
    if (!transacao.getItensUtilizados().isEmpty()) {
        caixa.getOrdensServico().add(transacao);
        System.out.println("Venda registrada com sucesso!");
        System.out.println("Valor total da venda: R$ " + transacao.getValor());
    } else {
        System.out.println("Venda cancelada - nenhum item adicionado.");
    }
}

    /**
     * Define a lista de funcionários da oficina.
     * @param funcionarios Nova lista de funcionários
     */
    public void setFuncionarios(List<Funcionario> funcionarios) {
        this.funcionarios = funcionarios;
    }

    /**
     * Define a lista de clientes da oficina.
     * @param clientes Nova lista de clientes
     */
    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    /**
     * Lista todos os veículos cadastrados na oficina.
     * Exibe os veículos agrupados por cliente.
     */
    public void listarTodosVeiculos() {
        System.out.println("\n=== TODOS OS VEÍCULOS ===");
        boolean encontrouVeiculos = false;
        
        for (Cliente cliente : getClientes()) {
            for (Veiculo veiculo : cliente.getVeiculos()) {
                System.out.println("Cliente: " + cliente.getNome());
                System.out.println(veiculo);
                System.out.println();
                encontrouVeiculos = true;
            }
        }
        
        if (!encontrouVeiculos) {
            System.out.println("Nenhum veículo cadastrado.");
        }
    }

    /**
     * Obtém o array de elevadores da oficina.
     * @return Array de elevadores
     */
    public Elevador[] getElevadores() {
        return elevadores;
    }
}