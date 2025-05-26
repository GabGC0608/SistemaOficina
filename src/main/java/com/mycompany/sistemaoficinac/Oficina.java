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
        inicializarServicosPadrao();
    }

    /**
     * Inicializa a lista de serviços com serviços padrão.
     */
    public void inicializarServicosPadrao() {
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
     * @param isEntrada
     */
    public void registrarPonto(boolean  isEntrada) {
        
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

    
    /***
     * Retorna a lista de registros de ponto dos funcionários.
     * @return Lista de registros de ponto.
     */

    public List<PontoFuncionario> getRegistrosPonto() {
        System.out.println("\n=== REGISTROS DE PONTO ===");
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
    public void adicionarItemEstoque() {
        System.out.println("\n=== ADICIONAR ITEM ===");
        String codigo = lerString("Código: ");
        String nome = lerString("Nome: ");
        int quantidade = lerInteiro("Quantidade: ");
        
        estoque.adicionarItem(codigo, nome, quantidade);
    }

    /**
     * Remove um item do estoque.
     *
     * @param codigo Código do item a ser removido.
     */
    public void removerItemEstoque() {
        estoque.listarItens();
        String codigo = lerString("Digite o código do item a remover: ");;

        estoque.removerItem(codigo);
        System.out.println("Item removido do estoque!");
    }

    /**
     * Atualiza a quantidade de um item no estoque.
     *
     * @param codigo Código do item.
     * @param novaQuantidade Nova quantidade do item.
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
     * @param codigo
     * @param quantidade
     */
    public void venderItemEstoque() {
        System.out.println("\n=== VENDER ITEM ===");
        getEstoque().listarItens();
        String codigo = lerString("Código: ");
        int quantidade = lerInteiro("Quantidade: ");
        
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
    public Elevador obterElevadorDisponivel() {
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
    public void agendarServico() {
        
        
        // Verificar se há serviços que requerem elevador
       /*  boolean requerElevador = servicos.stream()
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
        */
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
        listarFuncionarios();
        String matriculaFuncionario = lerString("Digite a matrícula do funcionário: ");
        Funcionario funcionario = getFuncionarios().stream()
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
        if (getAgenda().verificarDisponibilidadeFuncionario(funcionario, dataHora)) {
            System.out.println("Este funcionário já possui um agendamento neste horário!");
            return;
        }

        String status = "A fazer"; 

        agenda.adicionarAgendamento(cliente, veiculo, servicos, funcionario, dataHora, status);
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
     * Adiciona um veículo a um cliente existente.
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

    /***
     * Método que retorna o contador de veículos.
     */
    protected void incrementarContadorVeiculos() {
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
    public void listarTodosVeiculos() {
        System.out.println("\n=== TODOS OS VEÍCULOS ===");
        boolean encontrouVeiculos = false;

        for (Cliente cliente : getClientes()) {
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
        public void buscarVeiculoPorPlaca() {
            String placa = lerString("Digite a placa do veículo: ");
           getClientes().stream()
                .flatMap(cliente -> cliente.getVeiculos().stream())
                .filter(v -> ((Veiculo)v).getPlaca().equalsIgnoreCase(placa))
                .forEach(System.out::println);
        }




    // Métodos para gerenciar funcionários
    /**
     * Contrata um novo funcionário.
     *
     * @param funcionario Funcionário a ser contratado.
     */
    public void contratarFuncionario() {
        System.out.println("\n=== CADASTRAR FUNCIONÁRIO ===");
        String nome = lerString("Nome: ");
        String telefone = lerString("Telefone: ");
        String endereco = lerString("Endereço: ");
        String cargo = lerString("Cargo: ");
        double salario = lerDouble("Salário: ");
        String matricula = lerString("Matrícula: ");
        
        Funcionario f1 = new Funcionario(nome, telefone, endereco, cargo, salario, matricula);

        if (cargo.equalsIgnoreCase("Administrador")) {
            f1 = new Administrador(nome, telefone, endereco, salario, matricula);
        } else {
            f1 = new Funcionario(nome, telefone, endereco, cargo, salario, matricula);
        }
        
        
        funcionarios.add(f1);
        System.out.println("Funcionário " + f1.getNome() + " contratado com sucesso!");
    }

    /**
     * Demite um funcionário com base na matrícula.
     *
     * @param matricula Matrícula do funcionário a ser demitido.
     */
    public void demitirFuncionario() {
        String matricula = lerString("Digite a matrícula do funcionário a ser removido: ");
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

    /**
     * Busca um funcionário pelo nome.
     */
    public void buscarFuncionarioPorNome() {
        String nome = lerString("Digite o nome do funcionário: ");
        getFuncionarios().stream()
            .filter(f -> f.getNome().equalsIgnoreCase(nome))
            .forEach(System.out::println);
    }
     /**
     * Mostra o registro de ponto.
     */  
    public void listarRegistrosPonto() {
        System.out.println("\n=== REGISTROS DE PONTO ===");
        getRegistrosPonto().forEach(System.out::println);
    }

    /**
 * Altera os dados de um funcionário existente.
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
     * Remove um cliente com base no telefone.
     *
     * @param telefone Telefone do cliente a ser removido.
     */
    public void removerCliente() {
        String telefone = lerString("Digite o telefone do cliente a ser removido: ");
        clientes.removeIf(c -> c.getTelefone().equals(telefone));
        System.out.println("Cliente com telefone " + telefone + " removido com sucesso!");
    }

    /**
     * Lista todos os clientes.
     * 
     */
    public void listarClientes() {
        System.out.println("\n=== CLIENTES ===");
        clientes.sort(Cliente.POR_NOME); // Ordena por nome antes de listar
        clientes.forEach(System.out::println);
    }
    /***
     *  Busca um cliente pelo nome.
     * @param nome Nome do cliente a ser buscado.
     */
    public void buscarClientePorNome() {
        String nome = lerString("Digite o nome do cliente: ");
        getClientes().stream()
            .filter(c -> c.getNome().equalsIgnoreCase(nome))
            .forEach(System.out::println);
    }

   /**
 * Altera os dados de um cliente existente.
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
     * Registra a compra de peças no estoque como despesa no caixa.
     * 
     * @param codigo Código do item
     * @param nome Nome do item
     * @param quantidade Quantidade comprada
     * @param precoUnitario Preço unitário do item
     * @param data Data da compra
     */
    public void registrarCompraPecas() {
        
        System.out.println("\n=== REGISTRAR COMPRA DE PEÇAS ===");
        String codigo = lerString("Código da peça: ");
        String nome = lerString("Nome da peça: ");
        int quantidade = lerInteiro("Quantidade: ");
        double precoUnitario = lerDouble("Preço unitário: ");
        String data = lerString("Data da compra (dd/MM/yyyy): ");
        
        double valorTotal = quantidade * precoUnitario;
        estoque.adicionarItem(codigo, nome, quantidade);
        caixa.registrarSaida(valorTotal, "Compra de peças: " + nome + " (" + quantidade + " un)", data,null,null,null);
    }

    /**
     * Registra o pagamento de salários como despesa no caixa.
     * 
     * @param data Data do pagamento
     */
    public void registrarPagamentoSalarios() {
        String data = lerString("Digite a data do pagamento (dd/MM/yyyy): ");
        
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
     * Remove um serviço com base no índice da lista.
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
     * Lista todos os serviços disponíveis com índices.
     */
    public void listarServicos() {
        System.out.println("\n=== SERVIÇOS DISPONÍVEIS ===");
        for (int i = 0; i < servicos.size(); i++) {
            System.out.println((i + 1) + ". " + servicos.get(i));
        }
    }
    /**
     * Lista todos os agendamentos.
     */
    public void listarAgendamentos() {
        agenda.listarAgendamentos();
    }
    /**
     * Busca um serviço pelo nome.
     */
    public void listarAgendamentosPorCliente() {
        String nome = lerString("Digite o nome do cliente: ");
        getAgenda().listarAgendamentosPorCliente(nome);
    }
    /**
     * Lista os agendamentos por status.
     */
    public void listarAgendamentosPorStatus() {
        String status = lerString("Digite o status (Agendado/Concluído/Cancelado): ");
        getAgenda().listarAgendamentosPorStatus(status);
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
    public void registrarPagamento() {
        
        double valor = lerDouble("Valor: ");
        String descricao = lerString("Descrição: ");
        String data = lerString("Data (dd/MM/yyyy): ");

        caixa.registrarEntrada(valor, descricao, data,null,null,null);
    }

    /**
     * Registra uma despesa no caixa.
     *
     * @param valor Valor da despesa.
     * @param descricao Descrição da despesa.
     * @param data Data da despesa.
     */
    public void registrarDespesa() {
        double valor = lerDouble("Valor: ");
        String descricao = lerString("Descrição: ");
        String data = lerString("Data (dd/MM/yyyy): ");
        
        caixa.registrarSaida(valor, descricao, data,null,null,null);
    }

    /**
     * Gera um relatório diário das transações financeiras.
     *
     * @param data Data do relatório.
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
     * Gera um relatório mensal das transações financeiras.
     *
     * @param mes Mês do relatório.
     * @param ano Ano do relatório.
     */
    public void gerarRelatorioMensal() {
        int mes = lerInteiro("Digite o mês (1-12): ");
        int ano = lerInteiro("Digite o ano: ");
        
        caixa.gerarRelatorioMensal(mes, ano);
    }

     public void listarTodasTransacoes() {
        System.out.println("\n=== TODAS AS TRANSAÇÕES ===");
        List<Caixa.Transacao> transacoes = getCaixa().getTransacoes();
        
        if (transacoes.isEmpty()) {
            System.out.println("Nenhuma transação registrada.");
            return;
        }
        
        // Formatar a saída para melhor visualização
        System.out.println("Data       | Tipo    | Valor    | Categoria       | Descrição");
        System.out.println("---------------------------------------------------------------");
        for (Caixa.Transacao t : transacoes) {
            System.out.printf("%-10s | %-7s | R$ %-6.2f | %-15s | %s\n",
                    t.getData(),
                    t.getTipo(),
                    t.getValor(),
                    t.getCategoria(),
                    t.getDescricao());
        }
        
        double saldoAtual = getCaixa().getSaldo();
        System.out.printf("\nTotal: %d transações | Saldo atual: R$ %.2f\n",
                        transacoes.size(), saldoAtual);
    }

    public void listarTransacoesPorPeriodo() {
        System.out.println("\n=== FILTRAR POR PERÍODO ===");
        String dataInicio = lerString("Data inicial (dd/MM/yyyy): ");
        String dataFim = lerString("Data final (dd/MM/yyyy): ");
        
        List<Caixa.Transacao> filtradas = getCaixa().getTransacoes().stream()
                .filter(t -> t.getData().compareTo(dataInicio) >= 0 && t.getData().compareTo(dataFim) <= 0)
                .collect(Collectors.toList());
        
        if (filtradas.isEmpty()) {
            System.out.println("Nenhuma transação no período especificado.");
            return;
        }
        
        System.out.println("\nTransações no período de " + dataInicio + " a " + dataFim + ":");
        System.out.println("Data       | Tipo    | Valor    | Categoria       | Descrição");
        System.out.println("---------------------------------------------------------------");
        for (Caixa.Transacao t : filtradas) {
            System.out.printf("%-10s | %-7s | R$ %-6.2f | %-15s | %s\n",
                    t.getData(),
                    t.getTipo(),
                    t.getValor(),
                    t.getCategoria(),
                    t.getDescricao());
        }
        
        double totalEntradas = filtradas.stream()
                .filter(t -> t.getTipo().equalsIgnoreCase("Entrada"))
                .mapToDouble(Caixa.Transacao::getValor)
                .sum();
        
        double totalSaidas = filtradas.stream()
                .filter(t -> t.getTipo().equalsIgnoreCase("Saída"))
                .mapToDouble(Caixa.Transacao::getValor)
                .sum();
        
        System.out.printf("\nResumo do período:\n");
        System.out.printf("Total de entradas: R$ %.2f\n", totalEntradas);
        System.out.printf("Total de saídas:   R$ %.2f\n", totalSaidas);
        System.out.printf("Saldo líquido:     R$ %.2f\n", (totalEntradas - totalSaidas));
    }

    public void listarTransacoesPorCategoria() {
        System.out.println("\n=== FILTRAR POR CATEGORIA ===");
        
        // Mostrar categorias disponíveis
        List<String> categorias = getCaixa().getTransacoes().stream()
                .map(t -> t.getCategoria())
                .distinct()
                .collect(Collectors.toList());
        
        System.out.println("Categorias disponíveis:");
        categorias.forEach(System.out::println);
        
        String categoria = lerString("\nDigite a categoria: ");
        
        List<Caixa.Transacao> filtradas = getCaixa().getTransacoes().stream()
                .filter(t -> t.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
        
        if (filtradas.isEmpty()) {
            System.out.println("Nenhuma transação nesta categoria.");
            return;
        }
        
        System.out.println("\nTransações na categoria '" + categoria + "':");
        System.out.println("Data       | Tipo    | Valor    | Descrição");
        System.out.println("-------------------------------------------");
        for (Caixa.Transacao t : filtradas) {
            System.out.printf("%-10s | %-7s | R$ %-6.2f | %s\n",
                    t.getData(),
                    t.getTipo(),
                    t.getValor(),
                    t.getDescricao());
        }
        
        double total = filtradas.stream()
                .mapToDouble(Caixa.Transacao::getValor)
                .sum();
        
        System.out.printf("\nTotal na categoria '%s': R$ %.2f (%d transações)\n",
                categoria, total, filtradas.size());
    }

    public void listarTransacoesPorTipo() {
        System.out.println("\n=== FILTRAR POR TIPO ===");
        String tipo = lerString("Digite o tipo (Entrada/Saída): ");
        
        if (!tipo.equalsIgnoreCase("Entrada") && !tipo.equalsIgnoreCase("Saída")) {
            System.out.println("Tipo inválido! Use 'Entrada' ou 'Saída'.");
            return;
        }
        
        List<Caixa.Transacao> filtradas = getCaixa().getTransacoes().stream()
                .filter(t -> t.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
        
        if (filtradas.isEmpty()) {
            System.out.println("Nenhuma transação deste tipo.");
            return;
        }
        
        System.out.println("\nTransações do tipo '" + tipo + "':");
        System.out.println("Data       | Valor    | Categoria       | Descrição");
        System.out.println("---------------------------------------------------");
        for (Caixa.Transacao t : filtradas) {
            System.out.printf("%-10s | R$ %-6.2f | %-15s | %s\n",
                    t.getData(),
                    t.getValor(),
                    t.getCategoria(),
                    t.getDescricao());
        }
        
        double total = filtradas.stream()
                .mapToDouble(Caixa.Transacao::getValor)
                .sum();
        
        System.out.printf("\nTotal de '%s': R$ %.2f (%d transações)\n",
                tipo, total, filtradas.size());
    }

    public void listarTransacoesPorResponsavel() {
        System.out.println("\n=== FILTRAR POR RESPONSÁVEL ===");
        String responsavel = lerString("Digite o nome ou matrícula do responsável: ");
        
        List<Caixa.Transacao> filtradas = getCaixa().getTransacoes().stream()
                .filter(t -> t.getResponsavel().toLowerCase().contains(responsavel.toLowerCase()))
                .collect(Collectors.toList());
        
        if (filtradas.isEmpty()) {
            System.out.println("Nenhuma transação encontrada para este responsável.");
            return;
        }
        
        System.out.println("\nTransações do responsável '" + responsavel + "':");
        System.out.println("Data       | Tipo    | Valor    | Categoria       | Descrição");
        System.out.println("---------------------------------------------------------------");
        for (Caixa.Transacao t : filtradas) {
            System.out.printf("%-10s | %-7s | R$ %-6.2f | %-15s | %s\n",
                    t.getData(),
                    t.getTipo(),
                    t.getValor(),
                    t.getCategoria(),
                    t.getDescricao());
        }
        
        System.out.printf("\nTotal para '%s': %d transações\n",
                responsavel, filtradas.size());
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
    // Métodos auxiliares para leitura de dados
    /***
     * Lê uma string do console.
     * @param mensagem
     * @return
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

    /***
     * Lê uma string do console.
     * @param mensagem
     * @return
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
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return A string lida do console
     */
    public static String lerString(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine();
    }
    /**
     * Lê um inteiro do console.
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return O inteiro lido do console
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
     * 
     * @param mensagem Mensagem a ser exibida ao usuário
     * @return O número decimal lido do console
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

      /**
 * Salva os dados de todas as classes em arquivos JSON separados.
 * @throws IOException Se ocorrer um erro durante a escrita nos arquivos.
 */
    public void salvarDados() throws IOException {
        // Usando a classe JsonUtil que já tem a configuração de pretty print
        
        JsonUtil.salvarParaJson(getCaixa().getTransacoes(), "data/transacoes.json");
        
        /*JsonUtil.salvarParaJson(oficina, "data/oficina.json");
        System.out.println("Dados da oficina salvos com sucesso!");
        */
        JsonUtil.salvarParaJson(getRegistrosPonto(), "data/pontos.json");
        System.out.println("Dados de ponto salvos com sucesso!");

        JsonUtil.salvarParaJson(loginManager, "data/login.json");
        System.out.println("Dados de login salvos com sucesso!");

        JsonUtil.salvarParaJson(contadorVeiculos, "data/contadorVeiculos.json");
        System.out.println("Contador de veículos salvo com sucesso!");

        JsonUtil.salvarParaJson(getClientes(), "data/clientes.json");
        System.out.println("Dados dos clientes salvos com sucesso!");

        JsonUtil.salvarParaJson(getFuncionarios(), "data/funcionarios.json");
        System.out.println("Dados dos funcionários salvos com sucesso!");

        JsonUtil.salvarParaJson(getServicos(), "data/servicos.json");
        System.out.println("Dados dos serviços salvos com sucesso!");

        JsonUtil.salvarParaJson(getEstoque(), "data/estoque.json");
        System.out.println("Dados do estoque salvos com sucesso!");

        JsonUtil.salvarParaJson(getAgenda(), "data/agenda.json");
        System.out.println("Dados da agenda salvos com sucesso!");

        JsonUtil.salvarParaJson(getCaixa(), "data/caixa.json");
        System.out.println("Dados do caixa salvos com sucesso!");
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

        if (!arquivoOficina.exists() || !arquivoLogin.exists()) {
            System.out.println("Arquivos de dados não encontrados. Criando novo sistema...");
            //oficina = new Oficina();
            loginManager = new Login();
            contadorVeiculos = 0;
            return;
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
    


}