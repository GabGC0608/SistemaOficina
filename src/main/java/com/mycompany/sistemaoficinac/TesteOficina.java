package com.mycompany.sistemaoficinac;

import java.util.*;
import java.io.IOException;

/**
 * Classe de teste para demonstrar todas as funcionalidades do sistema de oficina.
 * Esta classe implementa testes abrangentes para validar o funcionamento do sistema,
 * incluindo gerenciamento de clientes, funcionários, veículos, ordens de serviço e estoque.
 * 
 * A classe está organizada em 18 questões principais, cada uma testando diferentes
 * aspectos do sistema de oficina. Os testes são executados sequencialmente e
 * demonstram o funcionamento completo do sistema.
 */
public class TesteOficina {
    
    /** Contador para controle de veículos no sistema */
    private static int contadorVeiculosPrivate = 0;

    /** Instância do sistema de oficina */
    private static Oficina oficina = new Oficina();
    

   

    /**
     * Método principal que executa todos os testes do sistema.
     * Organizado em blocos de código correspondentes a diferentes aspectos do sistema.
     */
    public static void main(String[] args) {
        // Inicializa dados de demonstração do sistema
        oficina.inicializarDadosDemonstracao();
        System.out.println("=== TESTE DO SISTEMA DE OFICINA ===\n");

        // Questão 1: Implementação das Classes
        {
            System.out.println("\n=== QUESTÃO 1: IMPLEMENTAÇÃO DAS CLASSES ===");
            System.out.println("\nTestando implementação das classes principais:");
            
            // Exibir dados de demonstração
            System.out.println("\nClientes cadastrados:");
            oficina.getClientes().forEach(System.out::println);
            
            System.out.println("\nFuncionários cadastrados:");
            oficina.getFuncionarios().forEach(System.out::println);
            
            System.out.println("\nVeículos cadastrados:");
            oficina.listarVeiculos();
            
            System.out.println("\nOrdens de serviço cadastradas:");
            oficina.getOrdensServico().forEach(System.out::println);
        }

        // Questão 2: Sistema para Colaboradores e Administrador
        {
            System.out.println("\n=== QUESTÃO 2: SISTEMA PARA COLABORADORES E ADMINISTRADOR ===");
            System.out.println("\nTestando sistema para colaboradores e administrador:");
            
            // Listar funcionários
            System.out.println("\nLista de funcionários:");
            oficina.listarFuncionarios();
            
            // Listar especialistas
            System.out.println("\nLista de especialistas:");
            oficina.listarEspecialistas();
        }

        // Questão 3: toString() e Construtores
        {
            System.out.println("\n=== QUESTÃO 3: toString() E CONSTRUTORES ===");
            System.out.println("\nTestando toString() e construtores:");
            
            // Exibir toString() de diferentes objetos
            System.out.println("\nExemplo de Cliente:");
            System.out.println(oficina.getClientes().get(0));
            
            System.out.println("\nExemplo de Funcionário:");
            System.out.println(oficina.getFuncionarios().get(0));
            
            System.out.println("\nExemplo de Veículo:");
            System.out.println(oficina.getClientes().get(0).getVeiculos().get(0));
        }

        // Questão 4: Construtores
        {
            System.out.println("\n=== QUESTÃO 4: CONSTRUTORES ===");
            System.out.println("\nTestando construtores:");
            
            // Exibir objetos criados com diferentes construtores
            System.out.println("\nExemplo de Cliente:");
            System.out.println(oficina.getClientes().get(1));
            
            System.out.println("\nExemplo de Funcionário:");
            System.out.println(oficina.getFuncionarios().get(1));
        }

        // Questão 5: Elevadores Estáticos
        {
            System.out.println("\n=== QUESTÃO 5: ELEVADORES ESTÁTICOS ===");
            System.out.println("\nTestando gerenciamento de elevadores:");
            
            // Testar elevadores
            Elevador[] elevadores = oficina.getElevadores();
            System.out.println("Número de elevadores: " + elevadores.length);
            
            // Testar alocação de elevador
            Elevador elevador = oficina.obterElevadorDisponivel();
            if (elevador != null) {
                System.out.println("Elevador disponível: " + elevador);
                elevador.setPeso(1500.0f);
                System.out.println("Elevador alocado com peso: " + elevador.getPeso());
            }
            
            int k = 1;
            // Testar liberação de elevador
            Elevador.liberarElevador(k);
            System.out.println("Elevador liberado");
        }

        // Questão 6: Gerenciamento de Colaboradores
        {
            System.out.println("\n=== QUESTÃO 6: GERENCIAMENTO DE COLABORADORES ===");
            System.out.println("\nTestando gerenciamento de colaboradores:");
            
            // Listar funcionários
            System.out.println("\nLista de funcionários:");
            oficina.listarFuncionarios();
            
            // Buscar funcionário por nome
            String nomeBusca = oficina.getFuncionarios().get(0).getNome();
            Funcionario funcionario = oficina.buscarFuncionarioPorNome(nomeBusca);
            if (funcionario != null) {
                System.out.println("\nFuncionário encontrado: " + funcionario);
            }
        }

        // Questão 7: Gerenciamento de Clientes
        {
            System.out.println("\n=== QUESTÃO 7: GERENCIAMENTO DE CLIENTES ===");
            System.out.println("\nTestando gerenciamento de clientes:");
            
            // Listar clientes
            System.out.println("\nLista de clientes:");
            oficina.getClientes().forEach(System.out::println);
            
            // Listar veículos
            System.out.println("\nLista de veículos:");
            oficina.listarVeiculos();
        }

        // Questão 8: Ordens de Serviço
        {
            System.out.println("\n=== QUESTÃO 8: ORDENS DE SERVIÇO ===");
            System.out.println("\nTestando ordens de serviço:");
            
            // Listar ordens de serviço
            System.out.println("\nLista de ordens de serviço:");
            oficina.getOrdensServico().forEach(System.out::println);
        }

        // Questão 9: Estoque
        {
            System.out.println("\n=== QUESTÃO 9: ESTOQUE ===");
            System.out.println("\nTestando gerenciamento de estoque:");
            
            // Listar itens do estoque
            System.out.println("\nLista de itens no estoque:");
            oficina.getEstoque().listarItens();
        }

        // Questão 10: Extrato
        {
            System.out.println("\n=== QUESTÃO 10: EXTRATO ===");
            System.out.println("\nTestando geração de extrato:");
            
            // Listar transações
            System.out.println("\nLista de transações:");
            oficina.listarTodasTransacoes();
        }

        // Questão 11: Variáveis Estáticas
        {
            System.out.println("\n=== QUESTÃO 11: VARIÁVEIS ESTÁTICAS ===");
            System.out.println("\nTestando variáveis estáticas:");
            
            // Exibir contador de veículos
            System.out.println("Valor do contador de veículos: " + oficina.getContadorVeiculosPrivate());
        }

        // Questão 12: Método de Classe
        {
            System.out.println("\n=== QUESTÃO 12: MÉTODO DE CLASSE ===");
            System.out.println("\nTestando método de classe:");
            
            // Exibir contador de veículos
            System.out.println("Valor do método de classe: " + SistemaOficinac.getContadorVeiculosPrivate());
        }

        // Questão 13: Comparator
        {
            System.out.println("\n=== QUESTÃO 13: COMPARATOR ===");
            System.out.println("\nTestando comparadores:");
            
            // Ordenar clientes por nome
            List<Cliente> clientes = new ArrayList<>(oficina.getClientes());
            Collections.sort(clientes, new ClientePorNome());
            System.out.println("\nClientes ordenados por nome:");
            clientes.forEach(System.out::println);
            
            // Ordenar clientes por quantidade de veículos
            Collections.sort(clientes, new ClientePorQtdVeiculos());
            System.out.println("\nClientes ordenados por quantidade de veículos:");
            clientes.forEach(System.out::println);
        }

        // Questão 14: Persistência
        {
            System.out.println("\n=== QUESTÃO 14: PERSISTÊNCIA ===");
            System.out.println("\nTestando persistência de dados:");
            
            try {
                // Salvar dados
                System.out.println("Salvando dados...");
                oficina.salvarDados();
                System.out.println("Dados salvos com sucesso!");
                
                // Carregar dados
                System.out.println("\nCarregando dados...");
                oficina.carregarDados();
                System.out.println("Dados carregados com sucesso!");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao manipular dados: " + e.getMessage());
            }
        }

        // Questão 15: Teste do Iterator
        System.out.println("\n=== Teste do Iterator ===");
        System.out.println("Listando clientes usando Iterator:");
        Iterator<Cliente> iterator = oficina.getClientes().iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        
        // Teste do foreach
        System.out.println("\nListando clientes usando foreach:");
        for (Cliente cliente : oficina.getClientes()) {
            System.out.println(cliente);
        }
        
        // Questão 16: Teste do Comparator
        System.out.println("\n=== Teste do Comparator ===");
        List<Cliente> clientes = new ArrayList<>(oficina.getClientes());
        
        // Ordenando por nome
        System.out.println("\nOrdenando clientes por nome:");
        Collections.sort(clientes, new Comparator<Cliente>() {
            @Override
            public int compare(Cliente c1, Cliente c2) {
                return c1.getNome().compareTo(c2.getNome());
            }
        });
        clientes.forEach(System.out::println);
        
        // Ordenando por CPF
        System.out.println("\nOrdenando clientes por CPF:");
        Collections.sort(clientes, new Comparator<Cliente>() {
            @Override
            public int compare(Cliente c1, Cliente c2) {
                return c1.getCpfAnonimizado().compareTo(c2.getCpfAnonimizado());
            }
        });
        clientes.forEach(System.out::println);
        
        // Questão 17: Teste do método find e binarySearch
        System.out.println("\n=== Teste do método find e binarySearch ===");
        String nomeBusca = "João Silva";
        
        // Usando método find com iterator
        Cliente clienteEncontrado = findClientePorNome(oficina.getClientes(), nomeBusca);
        System.out.println("\nCliente encontrado pelo método find:");
        System.out.println(clienteEncontrado != null ? clienteEncontrado : "Cliente não encontrado");
        
        // Usando binarySearch
        Collections.sort(clientes, new Comparator<Cliente>() {
            @Override
            public int compare(Cliente c1, Cliente c2) {
                return c1.getNome().compareTo(c2.getNome());
            }
        });
        
        int index = Collections.binarySearch(clientes, new Cliente(nomeBusca, "", "", ""), 
            new Comparator<Cliente>() {
                @Override
                public int compare(Cliente c1, Cliente c2) {
                    return c1.getNome().compareTo(c2.getNome());
                }
            });
        
        System.out.println("\nCliente encontrado pelo binarySearch:");
        System.out.println(index >= 0 ? clientes.get(index) : "Cliente não encontrado");
        
        // Questão 18: Demonstração do fluxo completo
        System.out.println("\n=== Demonstração do Fluxo Completo ===");
        demonstrarFluxoCompleto(oficina);
    }
    
    // Método find implementado usando iterator
    private static Cliente findClientePorNome(List<Cliente> clientes, String nome) {
        Iterator<Cliente> iterator = clientes.iterator();
        while (iterator.hasNext()) {
            Cliente cliente = iterator.next();
            if (cliente.getNome().equals(nome)) {
                return cliente;
            }
        }
        return null;
    }
    
    // Método para demonstrar o fluxo completo de atendimento
    private static void demonstrarFluxoCompleto(Oficina oficina) {
        // Cadastro de clientes
        System.out.println("\nCadastrando clientes...");
        for (int i = 1; i <= 10; i++) {
            // 1. Cadastro do cliente
            Cliente cliente = new Cliente(
                "Cliente " + i,
                "Rua " + i,
                "1234567890" + i,
                "cliente" + i + "@email.com"
            );
            oficina.getClientes().add(cliente);
            
            // 2. Cadastro do veículo
            Veiculo veiculo = new Veiculo(
                "Modelo " + i,
                "ABC" + i + "123",
                2020 + i,
                "Marca " + i,
                "Cor " + i
            );
            cliente.getVeiculos().add(veiculo);
            
            // 3. Cadastro do mecânico
            Funcionario mecanico = new Funcionario(
                "Mecânico " + i,
                "1234567890" + i,
                "Rua do Mecânico " + i,
                "Mecânico",
                2000.0 + (i * 100),
                "MAT" + i,
                "Mecânica",
                new ArrayList<>(),
                false
            );
            
            // 4. Criação da ordem de serviço
            OrdemServico ordem = new OrdemServico(
                cliente,
                veiculo,
                new ArrayList<>(),
                mecanico,
                "01/01/2024",
                "09:00"
            );
            
            // 5. Adição do serviço
            Servico servico = new Servico(
                "Serviço " + i,
                "Descrição do serviço " + i,
                100.0 * i,
                60 * i // tempo estimado em minutos
            );
            ordem.adicionarServico(servico);
            
            // 6. Marcar ordem como em andamento
            ordem.setStatus("Em Andamento");
            
            // 7. Adicionar itens do estoque
            Estoque.ItemEstoque item = new Estoque.ItemEstoque(
                "ITEM" + i,
                "Item " + i,
                5,
                50.0 * i,
                "Descrição do item " + i
            );
            ordem.adicionarItemEstoque(item, 2);
            
            // 8. Concluir a ordem de serviço
            ordem.setStatus("Concluído");
            
            // 9. Registrar no caixa
            oficina.getCaixa().registrarEntrada(ordem);
            
            System.out.println("\nOrdem de serviço criada para " + cliente.getNome());
            System.out.println("Valor do serviço: R$ " + servico.getValor());
            System.out.println("Valor dos itens: R$ " + (item.getPreco() * 2));
            System.out.println("Valor total: R$ " + ordem.getValor());
        }
        
        /*// 10. Gerar relatório mensal
        System.out.println("\nGerando relatório mensal...");
        oficina.gerarBalancoMensal();
    */
        }
} 