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
import java.time.Duration;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Classe que representa a oficina mecânica.
 * Gerencia todos os aspectos da oficina, incluindo:
 * - Elevadores para manutenção de veículos
 * - Funcionários e controle de ponto
 * - Clientes e seus veículos
 * - Serviços oferecidos
 * - Estoque de peças
 * - Caixa e finanças
 * - Ordens de serviço
 * 
 * @author Gabriel
 * @version 1.0
 * @since 2024
 */
public class Oficina {
    private Elevador[] elevadores; // Array de elevadores da oficina
    private List<Funcionario> funcionarios; // Lista de funcionários
    public List<Cliente> clientes; // Lista de clientes
    private List<Servico> servicos; // Lista de serviços disponíveis
    private Estoque estoque; // Estoque de itens
    private Caixa caixa; // Caixa financeiro da oficina
    private List<PontoFuncionario> registrosPonto; // Lista de registros de ponto dos funcionários
    private List<OrdemServico> ordensServico; // Lista de ordens de serviço
    
    private static Scanner scanner = new Scanner(System.in);
    private static Login loginManager = new Login(); // Inicializa com um objeto padrão
    protected int contadorVeiculosProtected = 0; // Contador de veículos (protected)
    private static int contadorVeiculosPrivate = 0; // Contador de veículos (private)

    /**
     * Construtor da classe Oficina.
     * Inicializa todos os atributos com valores padrão e configura o ambiente inicial.
     * 
     * @throws IllegalStateException se houver erro na inicialização dos elevadores
     * @throws RuntimeException se houver erro na inicialização dos serviços padrão
     */
    public Oficina() {
        try {
            this.elevadores = new Elevador[3];
            Elevador.inicializarElevadores();
            // Copy the static elevators to the instance array
            for (int i = 0; i < 3; i++) {
                this.elevadores[i] = Elevador.getElevador(i);
            }
            this.funcionarios = new ArrayList<>();
            this.clientes = new ArrayList<>();
            this.servicos = new ArrayList<>();
            this.caixa = new Caixa();
            this.estoque = new Estoque();
            this.registrosPonto = new ArrayList<>();
            this.ordensServico = new ArrayList<>();
            inicializarServicosPadrao();
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao inicializar a oficina: " + e.getMessage());
        }
    }

    /**
     * Inicializa a lista de serviços com serviços padrão da oficina.
     * Define os serviços básicos oferecidos com seus respectivos preços e tempos estimados.
     * 
     * @throws IllegalStateException se houver erro ao adicionar os serviços padrão
     */
    public void inicializarServicosPadrao() {
        try {
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
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao inicializar serviços padrão: " + e.getMessage());
        }
    }

    /**
     * Registra a entrada ou saída de um funcionário no ponto.
     * 
     * @param isEntrada true para registrar entrada, false para registrar saída
     * @throws IllegalArgumentException se a matrícula for inválida
     * @throws IllegalStateException se tentar registrar saída sem entrada correspondente
     */
    public void registrarPonto(boolean isEntrada) {
        try {
            String matricula = lerString("Digite a matrícula: ");
            if (matricula == null || matricula.trim().isEmpty()) {
                throw new IllegalArgumentException("Matrícula inválida");
            }

            // Verifica se o funcionário existe
            boolean funcionarioExiste = funcionarios.stream()
                .anyMatch(f -> f.getMatricula().equals(matricula));
            
            if (!funcionarioExiste) {
                throw new IllegalArgumentException("Funcionário não encontrado");
            }

            if (isEntrada) {
                // Verifica se já existe um registro de entrada sem saída
                boolean jaRegistrouEntrada = registrosPonto.stream()
                    .anyMatch(p -> p.getMatricula().equals(matricula) && p.getSaida() == null);
                
                if (jaRegistrouEntrada) {
                    throw new IllegalStateException("Funcionário já registrou entrada e não registrou saída");
                }

                registrosPonto.add(new PontoFuncionario(matricula, LocalDateTime.now(), null));
                System.out.println("Entrada registrada com sucesso!");
            } else {
                boolean registroEncontrado = false;
                for (int i = registrosPonto.size() - 1; i >= 0; i--) {
                    PontoFuncionario ponto = registrosPonto.get(i);
                    if (ponto.getMatricula().equals(matricula) && ponto.getSaida() == null) {
                        ponto.setSaida(LocalDateTime.now());
                        registroEncontrado = true;
                        System.out.println("Saída registrada com sucesso!");
                        break;
                    }
                }
                if (!registroEncontrado) {
                    throw new IllegalStateException("Não foi encontrado registro de entrada para esta matrícula");
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao registrar ponto: " + e.getMessage());
            throw new RuntimeException("Falha ao registrar ponto", e);
        }
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

    public void adicionarPecaAOrdemServico() {
    if (ordensServico.isEmpty()) {
        System.out.println("Nenhuma ordem de serviço cadastrada.");
        return;
    }

    // Listar ordens de serviço
    System.out.println("\n=== ORDENS DE SERVIÇO ===");
    for (int i = 0; i < ordensServico.size(); i++) {
        System.out.println((i + 1) + ". " + ordensServico.get(i));
    }

    int indice = lerInteiro("Digite o número da ordem de serviço para adicionar peças: ") - 1;
    if (indice < 0 || indice >= ordensServico.size()) {
        System.out.println("Índice inválido!");
        return;
    }

    OrdemServico ordem = ordensServico.get(indice);

    boolean continuar = true;
    while (continuar) {
        // Listar itens do estoque
        estoque.listarItens();
        String codigo = lerString("Digite o código da peça a adicionar (ou 0 para sair): ");
        if (codigo.equals("0")) break;

        Estoque.ItemEstoque item = null;
        for (Estoque.ItemEstoque it : estoque.getItens()) {
            if (it.getCodigo().equalsIgnoreCase(codigo)) {
                item = it;
                break;
            }
        }
        if (item == null) {
            System.out.println("Peça não encontrada!");
            continue;
        }

        int quantidade = lerInteiro("Digite a quantidade a adicionar: ");
        if (quantidade <= 0) {
            System.out.println("Quantidade inválida!");
            continue;
        }
        if (quantidade > item.getQuantidade()) {
            System.out.println("Quantidade insuficiente no estoque! Disponível: " + item.getQuantidade());
            continue;
        }

        try {
            ordem.adicionarItemEstoque(item, quantidade);
            item.setQuantidade(item.getQuantidade() - quantidade);
            System.out.println("Peça adicionada à ordem de serviço!");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao adicionar peça: " + e.getMessage());
        }

        String resp = lerString("Deseja adicionar outra peça? (S/N): ");
        continuar = resp.equalsIgnoreCase("S");
    }
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

        caixa.setSaldo(caixa.getSaldo()-(preco * quantidade)); // Atualiza o saldo do caixa com o valor do item
        
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
        System.out.println("\n=== AGENDAR ORDEM DE SERVIÇO ===");

        // Seleção do cliente e veículo (igual ao seu código atual)
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

        // Seleção dos serviços
        List<Servico> servicosSelecionados = new ArrayList<>();
        boolean adicionarMaisServicos = true;
        boolean precisaElevador = false;
        
        while (adicionarMaisServicos) {
            System.out.println("\nServiços disponíveis:");
            List<Servico> servicos = getServicos();
            for (int i = 0; i < servicos.size(); i++) {
                Servico servico = servicos.get(i);
                System.out.printf("%d. %s - R$%.2f (%d min)%s%n", 
                    i + 1, 
                    servico.getNome(), 
                    servico.getValor(),
                    servico.getTempoEstimado(),
                    servico.isRequerElevador() ? " [Requer Elevador]" : "");
            }

            int opcaoServico = lerInteiro("Digite o número do serviço a adicionar: ");
            if (opcaoServico < 1 || opcaoServico > servicos.size()) {
                System.out.println("Número de serviço inválido!");
                continue;
            }
            
            Servico servicoSelecionado = servicos.get(opcaoServico - 1);
            servicosSelecionados.add(servicoSelecionado);
            
            // Verifica se algum serviço requer elevador
            if (servicoSelecionado.isRequerElevador()) {
                precisaElevador = true;
            }
            
            System.out.println("Serviço adicionado: " + servicoSelecionado.getNome());

            String resposta = lerString("Deseja adicionar outro serviço? (S/N): ");
            adicionarMaisServicos = resposta.equalsIgnoreCase("S");
        }

        if (servicosSelecionados.isEmpty()) {
            System.out.println("Nenhum serviço selecionado!");
            return;
        }

        // Responsável
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

        // Data e hora
        String data = lerData("Digite a data do serviço (DD/MM/AAAA): ");
        String hora = lerHora("Digite a hora do serviço (HH:MM): ");

        // Verifica se o mecânico já tem serviço neste horário
        boolean mecanicoOcupado = ordensServico.stream()
            .filter(o -> o.getStatus().equalsIgnoreCase("Agendado") || o.getStatus().equalsIgnoreCase("Em Andamento"))
            .anyMatch(o -> o.getResponsavel().getMatricula().equals(funcionario.getMatricula()) 
                && o.getData().equals(data) 
                && o.getHorario().equals(hora));

        if (mecanicoOcupado) {
            System.out.println("ATENÇÃO: O mecânico já possui um serviço agendado neste horário!");
            String continuar = lerString("Deseja continuar mesmo assim? (S/N): ");
            if (!continuar.equalsIgnoreCase("S")) {
                System.out.println("Agendamento cancelado.");
                return;
            }
        }

        // Verifica disponibilidade de elevador se necessário
        final Elevador elevadorAlocado;
        if (precisaElevador) {
            System.out.println("\nVerificando disponibilidade de elevador...");
            elevadorAlocado = obterElevadorDisponivel();
            
            if (elevadorAlocado != null) {
                // Verifica se o elevador já está reservado neste horário
                boolean elevadorOcupado = ordensServico.stream()
                    .filter(o -> o.getStatus().equalsIgnoreCase("Agendado") || o.getStatus().equalsIgnoreCase("Em Andamento"))
                    .filter(o -> o.getElevadorAlocado() != null && o.getElevadorAlocado().equals(elevadorAlocado))
                    .anyMatch(o -> o.getData().equals(data) && o.getHorario().equals(hora));

                if (elevadorOcupado) {
                    System.out.println("ATENÇÃO: O elevador já está reservado para este horário!");
                    String continuar = lerString("Deseja continuar mesmo assim? (S/N): ");
                    if (!continuar.equalsIgnoreCase("S")) {
                        System.out.println("Agendamento cancelado.");
                        return;
                    }
                } else {
                    System.out.println("Elevador " + (Arrays.asList(elevadores).indexOf(elevadorAlocado) + 1) + " alocado com sucesso!");
                }
            } else {
                System.out.println("ATENÇÃO: Nenhum elevador disponível para o horário solicitado!");
                String continuar = lerString("Deseja continuar mesmo assim? (S/N): ");
                if (!continuar.equalsIgnoreCase("S")) {
                    System.out.println("Agendamento cancelado.");
                    return;
                }
            }
        } else {
            elevadorAlocado = null;
        }

        // Cria a ordem de serviço
        OrdemServico ordem = new OrdemServico(
            cliente,
            veiculo,
            servicosSelecionados,
            funcionario,
            data,
            hora
        );

        // Adiciona a ordem à lista
        ordensServico.add(ordem);
        System.out.println("\nOrdem de serviço agendada com sucesso!");
        System.out.println("Número da ordem: " + ordem.getNumeroOrdem());
        
        // Se um elevador foi alocado, registra na ordem
        if (elevadorAlocado != null) {
            ordem.setElevadorAlocado(elevadorAlocado);
            elevadorAlocado.setModelo(veiculo.getModelo());
            elevadorAlocado.setHorarioAlocacao(LocalDateTime.now());
            elevadorAlocado.adicionarRegistroHistorico(
                String.format("Alocado para ordem %s - Veículo: %s - Data: %s %s",
                    ordem.getNumeroOrdem(),
                    veiculo.getModelo(),
                    data,
                    hora)
            );
        }
    }

    /**
     * Cancela um agendamento existente.
     * Registra o valor retido (20% do valor total) no caixa como entrada.
     */
        public void cancelarOrdemServico() {
         System.out.println("\n=== CANCELAR ORDEM DE SERVIÇO ===");
    List<OrdemServico> pendentes = ordensServico.stream()
        .filter(o -> !"Concluído".equalsIgnoreCase(o.getStatus()) && !"Cancelado".equalsIgnoreCase(o.getStatus()))
        .collect(Collectors.toList());

    if (pendentes.isEmpty()) {
        System.out.println("Nenhuma ordem de serviço para cancelar.");
        return;
    }

    for (int i = 0; i < pendentes.size(); i++) {
        System.out.println((i + 1) + ". " + pendentes.get(i));
    }

    int index = lerInteiro("Digite o número da ordem a cancelar: ") - 1;
    if (index < 0 || index >= pendentes.size()) {
        System.out.println("Número inválido!");
        return;
    }

    OrdemServico ordem = pendentes.get(index);
    String confirmacao = lerString("Deseja realmente cancelar esta ordem? (S/N): ");
    if (confirmacao.equalsIgnoreCase("S")) {
        ordem.setStatus("Cancelado");
        double valorRetido = ordem.getValor() * 0.20;
        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        caixa.registrarEntrada(ordem);
        caixa.setSaldo(caixa.getSaldo() + valorRetido);
       
        System.out.println("Ordem de serviço cancelada! Valor retido registrado no caixa.");
    } else {
        System.out.println("Cancelamento não realizado.");
    }
}

    /**
     * Conclui uma ordem de serviço existente.
     * Atualiza o status da ordem e libera o elevador se estiver alocado.
     */
    public void concluirOrdemServico() {
        System.out.println("\n=== CONCLUIR ORDEM DE SERVIÇO ===");
        
        if (ordensServico.isEmpty()) {
            System.out.println("Não há ordens de serviço pendentes!");
            return;
        }

        System.out.println("\nOrdens de serviço pendentes:");
        for (OrdemServico ordem : ordensServico) {
            if (!ordem.getStatus().equalsIgnoreCase("Concluído")) {
                System.out.println("Número: " + ordem.getNumeroOrdem());
                System.out.println("Cliente: " + ordem.getCliente().getNome());
                System.out.println("Veículo: " + ordem.getVeiculo().getModelo());
                System.out.println("Data: " + ordem.getData());
                System.out.println("Hora: " + ordem.getHorario());
                System.out.println("Status: " + ordem.getStatus());
                System.out.println("----------------------------------------");
            }
        }

        String numeroOrdem = lerString("\nDigite o número da ordem a ser concluída: ");
        OrdemServico ordem = ordensServico.stream()
            .filter(o -> o.getNumeroOrdem().equals(numeroOrdem))
            .findFirst()
            .orElse(null);

        if (ordem == null) {
            System.out.println("Ordem de serviço não encontrada!");
            return;
        }

        if (ordem.getStatus().equalsIgnoreCase("Concluído")) {
            System.out.println("Esta ordem já foi concluída!");
            return;
        }

        // Libera o elevador se estiver alocado
        if (ordem.getElevadorAlocado() != null) {
            int indexElevador = Arrays.asList(elevadores).indexOf(ordem.getElevadorAlocado());
            if (indexElevador != -1) {
                Elevador.liberarElevador(indexElevador);
                System.out.println("Elevador " + (indexElevador + 1) + " liberado com sucesso!");
            }
        }

        ordem.setStatus("Concluído");
        System.out.println("Ordem de serviço concluída com sucesso!");
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
            contadorVeiculosPrivate++;
            contadorVeiculosProtected++; // Incrementa o contador de veículos
        } else {
            System.out.println("Cliente não encontrado!");
        }
    }

    /**
     * Remove um veículo de um cliente e decrementa o contador.
     * @param telefoneCliente Telefone do cliente
     * @param placaVeiculo Placa do veículo a ser removido
     */
    public void removerVeiculoDeCliente(String telefoneCliente, String placaVeiculo) {
        Cliente cliente = getClientes().stream()
            .filter(c -> c.getTelefone().equals(telefoneCliente))
            .findFirst()
            .orElse(null);
        
        if (cliente != null) {
            cliente.removerVeiculo(placaVeiculo);
            contadorVeiculosPrivate--;
            contadorVeiculosProtected--; // Decrementa o contador de veículos
        } else {
            System.out.println("Cliente não encontrado!");
        }
    }

    /**
     * Obtém o contador total de veículos cadastrados.
     * @return Número total de veículos cadastrados na oficina
     */
    public int getContadorVeiculosPrivate() {
        return contadorVeiculosPrivate;
    }

    /**
     * Obtém o contador total de veículos cadastrados (método estático).
     * @return Número total de veículos cadastrados na oficina
     */
    public  int getContadorVeiculosProtected() {
        return contadorVeiculosProtected;
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
     * Solicita e valida os dados do cliente antes de adicioná-lo à lista.
     * 
     * @throws IllegalArgumentException se os dados do cliente forem inválidos
     * @throws IllegalStateException se o cliente já estiver cadastrado
     */
    public void cadastrarCliente() {
        try {
            String nome = lerString("Digite o nome do cliente: ");
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome do cliente não pode ser vazio");
            }

            String telefone = lerString("Digite o telefone do cliente: ");
            if (telefone == null || telefone.trim().isEmpty()) {
                throw new IllegalArgumentException("Telefone do cliente não pode ser vazio");
            }

            // Verifica se o cliente já existe
            for (Cliente c : clientes) {
                if (c.getTelefone().equals(telefone)) {
                    throw new IllegalStateException("Cliente com este telefone já está cadastrado");
                }
            }

            String endereco = lerString("Digite o endereço do cliente: ");
            if (endereco == null || endereco.trim().isEmpty()) {
                throw new IllegalArgumentException("Endereço do cliente não pode ser vazio");
            }

            String cpf = lerString("Digite o CPF do cliente: ");
            if (cpf == null || cpf.trim().isEmpty()) {
                throw new IllegalArgumentException("CPF do cliente não pode ser vazio");
            }

            String cpfAnonimizado = anonimizarCPF(cpf);
            Cliente cliente = new Cliente(nome, telefone, endereco, cpfAnonimizado);
            clientes.add(cliente);
            System.out.println("Cliente " + cliente.getNome() + " cadastrado com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Erro ao cadastrar cliente: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao cadastrar cliente: " + e.getMessage());
            throw new RuntimeException("Falha ao cadastrar cliente", e);
        }
    }

    /**
     * Remove um cliente existente do sistema.
     * Verifica se existem ordens de serviço pendentes antes de remover.
     * 
     * @throws IllegalArgumentException se o telefone for inválido
     * @throws IllegalStateException se o cliente tiver ordens de serviço pendentes
     */
    public void removerCliente() {
        try {
            String telefone = lerString("Digite o telefone do cliente a ser removido: ");
            if (telefone == null || telefone.trim().isEmpty()) {
                throw new IllegalArgumentException("Telefone inválido");
            }

            // Verifica se existem ordens de serviço pendentes
            for (OrdemServico ordem : ordensServico) {
                if (ordem.getCliente().equals(telefone) && 
                    !ordem.getStatus().equals("CONCLUIDA")) {
                    throw new IllegalStateException(
                        "Não é possível remover o cliente pois existem ordens de serviço pendentes"
                    );
                }
            }

            boolean removido = clientes.removeIf(c -> c.getTelefone().equals(telefone));
            if (removido) {
                System.out.println("Cliente com telefone " + telefone + " removido com sucesso!");
            } else {
                System.out.println("Cliente não encontrado!");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Erro ao remover cliente: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao remover cliente: " + e.getMessage());
            throw new RuntimeException("Falha ao remover cliente", e);
        }
    }

    /**
     * Lista todos os clientes cadastrados no sistema.
     * Exibe as informações de forma organizada e legível.
     * 
     * @throws IllegalStateException se não houver clientes cadastrados
     */
    public void listarClientes() {
        try {
            if (clientes.isEmpty()) {
                throw new IllegalStateException("Não há clientes cadastrados no sistema");
            }

            System.out.println("\n=== CLIENTES CADASTRADOS ===");
            for (Cliente cliente : getClientes()) {
                System.out.println(cliente.toString());
            }
        } catch (IllegalStateException e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar clientes: " + e.getMessage());
            throw new RuntimeException("Falha ao listar clientes", e);
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
        try {
            String nome = lerString("Digite o nome do serviço: ");
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome do serviço não pode ser vazio");
            }

            // Verifica se o serviço já existe
            for (Servico s : servicos) {
                if (s.getNome().equalsIgnoreCase(nome)) {
                    throw new IllegalStateException("Serviço com este nome já está cadastrado");
                }
            }

            String descricao = lerString("Digite a descrição do serviço: ");
            if (descricao == null || descricao.trim().isEmpty()) {
                throw new IllegalArgumentException("Descrição do serviço não pode ser vazia");
            }

            double valor = lerDouble("Digite o valor do serviço: ");
            if (valor <= 0) {
                throw new IllegalArgumentException("Valor do serviço deve ser maior que zero");
            }

            int tempoEstimado = lerInteiro("Digite o tempo estimado em minutos: ");
            if (tempoEstimado <= 0) {
                throw new IllegalArgumentException("Tempo estimado deve ser maior que zero");
            }

            Servico servico = new Servico(nome, descricao, valor, tempoEstimado);
            servicos.add(servico);
            System.out.println("Serviço " + servico.getNome() + " adicionado com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Erro ao adicionar serviço: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao adicionar serviço: " + e.getMessage());
            throw new RuntimeException("Falha ao adicionar serviço", e);
        }
    }

        /**
     * Remove um serviço existente do catálogo.
     * Verifica se existem ordens de serviço pendentes antes de remover.
     * 
     * @throws IllegalArgumentException se o nome do serviço for inválido
     * @throws IllegalStateException se o serviço tiver ordens pendentes
     */
    public void removerServico() {
        try {
            String nome = lerString("Digite o nome do serviço a ser removido: ");
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome do serviço inválido");
            }

            // Verifica se existem ordens de serviço pendentes
            for (OrdemServico ordem : ordensServico) {
                for (Servico servico : ordem.getServicos()) {
                    if (servico.getNome().equalsIgnoreCase(nome) && 
                        !ordem.getStatus().equals("CONCLUIDA")) {
                        throw new IllegalStateException(
                            "Não é possível remover o serviço pois existem ordens pendentes"
                        );
                    }
                }
            }

            boolean removido = servicos.removeIf(s -> s.getNome().equalsIgnoreCase(nome));
            if (removido) {
                System.out.println("Serviço " + nome + " removido com sucesso!");
        } else {
                System.out.println("Serviço não encontrado!");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Erro ao remover serviço: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao remover serviço: " + e.getMessage());
            throw new RuntimeException("Falha ao remover serviço", e);
        }
    }

    /**
     * Lista todos os serviços disponíveis na oficina.
     * Exibe as informações de forma organizada e legível.
     * 
     * @throws IllegalStateException se não houver serviços cadastrados
     */
    public void listarServicos() {
        try {
            if (servicos.isEmpty()) {
                throw new IllegalStateException("Não há serviços cadastrados no sistema");
            }

            System.out.println("\n=== SERVIÇOS DISPONÍVEIS ===");
            for (Servico servico : servicos) {
                System.out.println(servico.toString());
            }
        } catch (IllegalStateException e) {
            System.err.println("Erro ao listar serviços: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar serviços: " + e.getMessage());
            throw new RuntimeException("Falha ao listar serviços", e);
        }
    }

    /**
     * Lista todas as ordens de serviço registradas no sistema.
     * Exibe as informações de forma organizada e legível.
     * 
     * @throws IllegalStateException se não houver ordens de serviço cadastradas
     */
    public void listarOrdens() {
        try {
            if (ordensServico.isEmpty()) {
                throw new IllegalStateException("Não há ordens de serviço cadastradas no sistema");
            }

            System.out.println("\n=== ORDENS DE SERVIÇO ===");
            for (OrdemServico ordem : ordensServico) {
                System.out.println(ordem.toString());
            }
        } catch (IllegalStateException e) {
            System.err.println("Erro ao listar ordens: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar ordens: " + e.getMessage());
            throw new RuntimeException("Falha ao listar ordens", e);
        }
    }

    /**
     * Lista todas as ordens de serviço de um cliente específico.
     * 
     * @throws IllegalArgumentException se o telefone do cliente for inválido
     * @throws IllegalStateException se o cliente não tiver ordens de serviço
     */
    public void listarOrdensPorCliente() {
        try {
            String telefone = lerString("Digite o telefone do cliente: ");
            if (telefone == null || telefone.trim().isEmpty()) {
                throw new IllegalArgumentException("Telefone do cliente inválido");
            }

            List<OrdemServico> ordensCliente = ordensServico.stream()
                .filter(o -> o.getCliente().equals(telefone))
                .collect(Collectors.toList());

            if (ordensCliente.isEmpty()) {
                throw new IllegalStateException("Cliente não possui ordens de serviço");
            }

            System.out.println("\n=== ORDENS DE SERVIÇO DO CLIENTE ===");
            for (OrdemServico ordem : ordensCliente) {
                System.out.println(ordem.toString());
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Erro ao listar ordens do cliente: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar ordens do cliente: " + e.getMessage());
            throw new RuntimeException("Falha ao listar ordens do cliente", e);
        }
    }

    /**
     * Lista todas as ordens de serviço com um status específico.
     * 
     * @throws IllegalArgumentException se o status for inválido
     * @throws IllegalStateException se não houver ordens com o status especificado
     */
    public void listarOrdensPorStatus() {
        try {
            System.out.println("\nStatus disponíveis:");
            System.out.println("1. Pendente");
            System.out.println("2. Em Andamento");
            System.out.println("3. Concluída");
            System.out.println("4. Cancelada");

            int opcao = lerInteiro("Digite o número do status desejado: ");
            String status;
            switch (opcao) {
                case 1: status = "PENDENTE"; break;
                case 2: status = "EM_ANDAMENTO"; break;
                case 3: status = "CONCLUIDA"; break;
                case 4: status = "CANCELADA"; break;
                default: throw new IllegalArgumentException("Opção de status inválida");
            }

            List<OrdemServico> ordensStatus = ordensServico.stream()
                .filter(o -> o.getStatus().equals(status))
                .collect(Collectors.toList());

            if (ordensStatus.isEmpty()) {
                throw new IllegalStateException("Não há ordens de serviço com o status " + status);
            }

            System.out.println("\n=== ORDENS DE SERVIÇO - " + status + " ===");
            for (OrdemServico ordem : ordensStatus) {
                System.out.println(ordem.toString());
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Erro ao listar ordens por status: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar ordens por status: " + e.getMessage());
            throw new RuntimeException("Falha ao listar ordens por status", e);
        }
    }

    /**
     * Lista todas as ordens de serviço de uma data específica.
     * 
     * @throws IllegalArgumentException se a data for inválida
     * @throws IllegalStateException se não houver ordens na data especificada
     */
    public void listarOrdensPorData() {
        try {
            String data = lerData("Digite a data (dd/mm/aaaa): ");
            if (data == null || data.trim().isEmpty()) {
                throw new IllegalArgumentException("Data inválida");
            }

            List<OrdemServico> ordensData = ordensServico.stream()
                .filter(o -> o.getData().equals(data))
                .collect(Collectors.toList());

            if (ordensData.isEmpty()) {
                throw new IllegalStateException("Não há ordens de serviço na data " + data);
            }

            System.out.println("\n=== ORDENS DE SERVIÇO - " + data + " ===");
            for (OrdemServico ordem : ordensData) {
                System.out.println(ordem.toString());
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Erro ao listar ordens por data: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar ordens por data: " + e.getMessage());
            throw new RuntimeException("Falha ao listar ordens por data", e);
        }
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
        System.out.println("\n=== Transações por Responsável ===");
        String responsavel = lerString("Digite o nome do responsável: ");
        
        List<OrdemServico> ordens = caixa.getOrdensServico().stream()
            .filter(t -> t.getResponsavel() != null && 
                        t.getResponsavel().getNome().equalsIgnoreCase(responsavel))
            .collect(Collectors.toList());
        
        if (ordens.isEmpty()) {
            System.out.println("Nenhuma transação encontrada para este responsável.");
            return;
        }

        System.out.println("\nTransações encontradas:");
        for (OrdemServico ordem : ordens) {
            System.out.println(ordem);
        }
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
                String mecanico = ordem.getResponsavel().getNome();
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
        System.out.println("\n=== Relatório de Desempenho dos Mecânicos ===");
        
        Map<String, Integer> servicosPorMecanico = new HashMap<>();
        Map<String, Double> faturamentoPorMecanico = new HashMap<>();
        
        for (OrdemServico ordem : caixa.getOrdensServico()) {
            if (ordem.getTipo().equals("Entrada")) {
                // Contabiliza serviços por mecânico
                String mecanico = ordem.getResponsavel().getNome();
                servicosPorMecanico.merge(mecanico, 1, Integer::sum);
                faturamentoPorMecanico.merge(mecanico, ordem.getValor(), Double::sum);
            }
        }
        
        if (servicosPorMecanico.isEmpty()) {
            System.out.println("Nenhum dado de desempenho disponível.");
            return;
        }

        System.out.println("\nDesempenho por Mecânico:");
        for (Map.Entry<String, Integer> entry : servicosPorMecanico.entrySet()) {
            String mecanico = entry.getKey();
            int servicos = entry.getValue();
            double faturamento = faturamentoPorMecanico.get(mecanico);
            
            System.out.printf("%nMecânico: %s%n", mecanico);
            System.out.printf("Total de Serviços: %d%n", servicos);
            System.out.printf("Faturamento Gerado: R$ %.2f%n", faturamento);
            System.out.printf("Ticket Médio: R$ %.2f%n", faturamento / servicos);
        }
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

    /**
     * Obtém a lista de serviços.
     *
     * @return Lista de serviços.
     */
    public List<Servico> getServicos() {
        return servicos;
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

    public void ordenarTudo(List<Cliente> clientes, List<OrdemServico> ordens, List<Servico> servicos) {
        // 1. Ordenar clientes alfabeticamente
        if (clientes != null) {
            clientes.sort(new Comparadores.ClientePorNome());
        }

        // 2. Ordenar agendamentos por data
        if (ordens != null) {
            ordens.sort(new Comparadores.ordemPorData());
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
    JsonUtil.salvarParaJson(getOrdensServico(), "data/ordensServico.json");
    JsonUtil.salvarParaJson(getRegistrosPonto(), "data/pontos.json");
    JsonUtil.salvarParaJson(loginManager, "data/login.json");
    JsonUtil.salvarParaJson(contadorVeiculosPrivate, "data/contadorVeiculos.json");
    JsonUtil.salvarParaJson(getClientes(), "data/clientes.json");
    JsonUtil.salvarParaJson(getFuncionarios(), "data/funcionarios.json");
    JsonUtil.salvarParaJson(getServicos(), "data/servicos.json");
    JsonUtil.salvarParaJson(getEstoque(), "data/estoque.json");
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
        File arquivoCaixa = new File("data/caixa.json");
        File arquivoPonto = new File("data/pontos.json");
        File arquivoOrdensServico = new File("data/ordensServico.json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        if(arquivoPonto.exists()){
            registrosPonto = mapper.readValue(arquivoPonto, new TypeReference<List<PontoFuncionario>>() {});
            System.out.println("Dados dos pontos carregados com sucesso!");
        } else {
            registrosPonto = new ArrayList<>();
        }
        
        if(arquivoOrdensServico.exists()){
            ordensServico = mapper.readValue(arquivoOrdensServico, new TypeReference<List<OrdemServico>>() {});
            System.out.println("Dados das ordens de serviço carregados com sucesso!");
        } else {
            ordensServico = new ArrayList<>();
        }

        // Carrega os dados de login
        if (arquivoLogin.exists()) {
            loginManager = mapper.readValue(arquivoLogin, Login.class);
            System.out.println("Dados de login carregados com sucesso!");
        } else {
            loginManager = new Login();
        }

        // Carrega o contador de veículos
        File arquivoContador = new File("data/contadorVeiculos.json");
        if (arquivoContador.exists()) {
            contadorVeiculosPrivate = mapper.readValue(arquivoContador, Integer.class);
            System.out.println("Contador de veículos carregado com sucesso!");
        } else {
            contadorVeiculosPrivate = 0;
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
        System.out.println("\n=== Venda de Peças ===");
        
        // Cria uma nova ordem de serviço para a venda
        OrdemServico transacao = new OrdemServico();
        transacao.setTipo("Entrada");
        transacao.setDescricao("Venda de peças");
        transacao.setData(Oficina.lerData("Digite a data (dd/MM/yyyy): "));
        transacao.setCategoria("Peças");
        
        String nomeVendedor = Oficina.lerString("Digite o nome do vendedor: ");
        Funcionario vendedor = buscarFuncionarioPorNome(nomeVendedor);
        if (vendedor == null) {
            System.out.println("Vendedor não encontrado!");
            return;
        }
        transacao.setResponsavel(vendedor);
        
        String nomeCliente = Oficina.lerString("Digite o nome do cliente: ");
        Cliente cliente = clientes.stream()
            .filter(c -> c.getNome().equalsIgnoreCase(nomeCliente))
            .findFirst()
            .orElse(null);
        if (cliente == null) {
            System.out.println("Cliente não encontrado!");
            return;
        }
        transacao.setCliente(cliente);
        
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

    

    /**
     * Interface para receber os dados do balanço mensal do usuário.
     * Solicita e valida o mês e ano desejados.
     */
    public void FazerBalancoMensal() {
        System.out.println("\n=== BALANÇO MENSAL ===");
        
        // Solicita e valida o mês
        int mes;
        do {
            mes = lerInteiro("Digite o mês desejado (1-12): ");
            if (mes < 1 || mes > 12) {
                System.out.println("Mês inválido! Por favor, digite um número entre 1 e 12.");
            }
        } while (mes < 1 || mes > 12);
        
        // Solicita e valida o ano
        int ano;
        do {
            ano = lerInteiro("Digite o ano desejado (ex: 2024): ");
            if (ano < 2000 || ano > 2100) {
                System.out.println("Ano inválido! Por favor, digite um ano entre 2000 e 2100.");
            }
        } while (ano < 2000 || ano > 2100);
        
        // Confirma com o usuário
        System.out.printf("\nDeseja gerar o balanço para %02d/%d? (S/N): ", mes, ano);
        String confirmacao = lerString("").toUpperCase();
        
        if (confirmacao.equals("S")) {
            caixa.gerarBalancoMensal(mes, ano);
        } else {
            System.out.println("Operação cancelada pelo usuário.");
        }
    }

    /**
     * Demonstra o status atual dos elevadores da oficina.
     * Mostra informações detalhadas sobre cada elevador, incluindo seu status e veículo alocado.
     */
    public void demonstrarElevadores() {
        System.out.println("\n=== STATUS DOS ELEVADORES ===");
        
        for (int i = 0; i < elevadores.length; i++) {
            Elevador elevador = elevadores[i];
            System.out.printf("\nElevador %d:%n", i + 1);
            
            if (elevador == null) {
                System.out.println("Status: Não inicializado");
                continue;
            }
            
            System.out.println("Status: " + (elevador.isOcupado() ? "Ocupado" : "Livre"));
            
            if (elevador.isOcupado()) {
                System.out.println("Veículo: " + elevador.getModelo());
                System.out.println("Horário de alocação: " + elevador.getHorarioAlocacao());
                
                // Calcula o tempo de ocupação
                LocalDateTime agora = LocalDateTime.now();
                Duration duracao = Duration.between(elevador.getHorarioAlocacao(), agora);
                long horas = duracao.toHours();
                long minutos = duracao.toMinutesPart();
                
                System.out.printf("Tempo de ocupação: %d horas e %d minutos%n", horas, minutos);
            }
            
            // Mostra histórico de uso
            System.out.println("\nHistórico de uso:");
            List<String> historico = elevador.getHistoricoUso();
            if (historico.isEmpty()) {
                System.out.println("Nenhum registro de uso.");
            } else {
                for (String registro : historico) {
                    System.out.println("- " + registro);
                }
            }
            
            System.out.println("----------------------------------------");
        }
        
        // Estatísticas gerais
        long elevadoresOcupados = Arrays.stream(elevadores)
            .filter(e -> e != null && e.isOcupado())
            .count();
            
        System.out.printf("%nResumo:%n");
        System.out.printf("Total de elevadores: %d%n", elevadores.length);
        System.out.printf("Elevadores ocupados: %d%n", elevadoresOcupados);
        System.out.printf("Elevadores livres: %d%n", elevadores.length - elevadoresOcupados);
        System.out.printf("Taxa de ocupação: %.1f%%%n", 
            (elevadoresOcupados * 100.0) / elevadores.length);
    }
}