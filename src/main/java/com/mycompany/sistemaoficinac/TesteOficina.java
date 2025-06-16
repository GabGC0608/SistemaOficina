package com.mycompany.sistemaoficinac;

import java.util.*;
import java.io.IOException;

/**
 * Classe de teste para demonstrar todas as funcionalidades do sistema de oficina.
 */
public class TesteOficina {
    private static Scanner scanner = new Scanner(System.in);
    private static int contadorVeiculosPrivate = 0;
    private static SistemaOficinac sistemaOficinac = new SistemaOficinac();

    public static void main(String[] args) {
        SistemaOficinac.inicializarDadosDemonstracao();
        Oficina oficina = SistemaOficinac.getOficina();
        System.out.println("=== TESTE DO SISTEMA DE OFICINA ===\n");

        // Questão 1: Implementação das Classes
        {
            System.out.println("\n=== QUESTÃO 1: IMPLEMENTAÇÃO DAS CLASSES ===");
            System.out.println("\nTestando implementação das classes principais:");
            
            // Teste da classe Cliente
            Cliente cliente = new Cliente("Pedro", "11999999999", "Rua C, 789", "987.654.321-00");
            System.out.println("Cliente criado: " + cliente);

            // Teste da classe Funcionario
            Funcionario funcionario = new Funcionario("Ana", "11988888888", "Rua D, 101", 
                "Mecânico", 3000.0, "MEC002", "Mecânica");
            System.out.println("Funcionário criado: " + funcionario);

            // Teste da classe Veiculo
            Veiculo veiculo = new Veiculo("Corolla", "XYZ5678", 2021, "Toyota", "Prata");
            System.out.println("Veículo criado: " + veiculo);

            // Teste da classe OrdemServico
            List<Servico> servicos = new ArrayList<>();
            servicos.add(new Servico("Revisão", "Revisão completa", 300.0, 120));
            OrdemServico ordem = new OrdemServico(cliente, veiculo, servicos, funcionario, 
                "2024-03-16", "10:00");
            System.out.println("Ordem de serviço criada: " + ordem);
        }

        // Questão 2: Sistema para Colaboradores e Administrador
        {
            System.out.println("\n=== QUESTÃO 2: SISTEMA PARA COLABORADORES E ADMINISTRADOR ===");
            System.out.println("\nTestando sistema para colaboradores e administrador:");
            
            // Adicionar colaborador
            Funcionario colaborador = new Funcionario("Roberto", "11955555555", "Rua G, 404", 
                "Mecânico", 2800.0, "MEC004", "Mecânica");
            oficina.getFuncionarios().add(colaborador);
            System.out.println("Colaborador adicionado: " + colaborador);

            // Alterar dados do colaborador
            colaborador.setSalario(3000.0);
            System.out.println("Dados do colaborador alterados: " + colaborador);

            // Remover colaborador
            oficina.getFuncionarios().remove(colaborador);
            System.out.println("Colaborador removido");
        }

        // Questão 3: toString() e Construtores
        {
            System.out.println("\n=== QUESTÃO 3: toString() E CONSTRUTORES ===");
            System.out.println("\nTestando toString() e construtores:");
            
            // Teste do toString() em diferentes classes
            Cliente cliente = new Cliente("Carlos", "11977777777", "Rua E, 202", "111.222.333-44");
            System.out.println("toString() do Cliente: " + cliente.toString());

            Funcionario funcionario = new Funcionario("Paulo", "11966666666", "Rua F, 303", 
                "Mecânico", 3500.0, "MEC003", "Mecânica");
            System.out.println("toString() do Funcionário: " + funcionario.toString());

            Veiculo veiculo = new Veiculo("HB20", "DEF9012", 2022, "Hyundai", "Branco");
            System.out.println("toString() do Veículo: " + veiculo.toString());
        }
        // Questão 4:
        {
            System.out.println("\n=== QUESTÃO 4: CONSTRUTORES ===");
            System.out.println("\nTestando construtores:");
            
            // Teste do construtor da classe Cliente
            Cliente cliente = new Cliente("Carlos", "11977777777", "Rua E, 202", "111.222.333-44");
            System.out.println("Cliente criado: " + cliente);
            
            // Teste do construtor da classe Funcionario
            Funcionario funcionario = new Funcionario("Paulo", "11966666666", "Rua F, 303", 
                "Mecânico", 3500.0, "MEC003", "Mecânica");
            System.out.println("Funcionário criado: " + funcionario);
            
            //os construtores das classes Cliente, Funcionario usam o super para se referir ao construtor da classe Pessoa
        }

        // Questão 5: Elevadores Estáticos
        {
            System.out.println("\n=== QUESTÃO 5: ELEVADORES ESTÁTICOS ===");
            System.out.println("\nTestando gerenciamento de elevadores:");
            
            // Teste dos elevadores estáticos
            Elevador[] elevadores = oficina.getElevadores();
            System.out.println("Número de elevadores: " + elevadores.length);

            // Teste de alocação de elevador
            Elevador elevador = oficina.obterElevadorDisponivel();
            if (elevador != null) {
                System.out.println("Elevador disponível: " + elevador);
                elevador.setPeso(1500.0f);
                System.out.println("Elevador alocado com peso: " + elevador.getPeso());
            }

            // Teste de liberação de elevador
            oficina.liberarElevador();
            System.out.println("Elevador liberado");
        }

        // Questão 6: Gerenciamento de Colaboradores
        {
            System.out.println("\n=== QUESTÃO 6: GERENCIAMENTO DE COLABORADORES ===");
            System.out.println("\nTestando gerenciamento de colaboradores:");
            
            // Adicionar colaborador
            Funcionario colaborador = new Funcionario("Roberto", "11955555555", "Rua G, 404", 
                "Mecânico", 2800.0, "MEC004", "Mecânica");
            oficina.getFuncionarios().add(colaborador);
            System.out.println("Colaborador adicionado: " + colaborador);

            // Alterar dados do colaborador
            colaborador.setSalario(3000.0);
            System.out.println("Dados do colaborador alterados: " + colaborador);

            // Remover colaborador
            oficina.getFuncionarios().remove(colaborador);
            System.out.println("Colaborador removido");
        }

        // Questão 7: Gerenciamento de Clientes
        {
            System.out.println("\n=== QUESTÃO 7: GERENCIAMENTO DE CLIENTES ===");
            System.out.println("\nTestando gerenciamento de clientes:");
            
            // Adicionar cliente
            Cliente cliente = new Cliente("Fernando", "11944444444", "Rua H, 505", "555.666.777-88");
            oficina.getClientes().add(cliente);
            System.out.println("Cliente adicionado: " + cliente);

            // Alterar dados do cliente
            cliente.setTelefone("11933333333");
            System.out.println("Dados do cliente alterados: " + cliente);

            // Remover cliente
            oficina.getClientes().remove(cliente);
            System.out.println("Cliente removido");
        }

        // Questão 8: 
        {
            System.out.println("\n=== QUESTÃO 8: ORDENS DE SERVIÇO ===");
            System.out.println("\nTestando ordens de serviço:");
            
            // Criar ordem de serviço
            Cliente cliente = new Cliente("Ricardo", "11922222222", "Rua I, 606", "999.888.777-66");
            Veiculo veiculo = new Veiculo("Onix", "GHI3456", 2023, "Chevrolet", "Vermelho");
            Funcionario mecanico = new Funcionario("José", "11911111111", "Rua J, 707", 
                "Mecânico", 3200.0, "MEC005", "Mecânica");
            
            List<Servico> servicos = new ArrayList<>();
            servicos.add(new Servico("Troca de óleo", "Troca de óleo e filtro", 150.0, 60));
            
            OrdemServico ordem = new OrdemServico(cliente, veiculo, servicos, mecanico, 
                "2024-03-17", "11:00");
            oficina.getOrdensServico().add(ordem);
            System.out.println("Ordem de serviço criada: " + ordem);

            // Verificar dados da ordem
            System.out.println("Cliente: " + ordem.getCliente());
            System.out.println("Veículo: " + ordem.getVeiculo());
            System.out.println("Serviços: " + ordem.getServicos());
            System.out.println("Valor total: R$ " + ordem.getValor());
        }

        // Questão 9:
        {
            System.out.println("\n=== QUESTÃO 9: ESTOQUE ===");
            System.out.println("\nTestando gerenciamento de estoque:");
            
            // Adicionar itens ao estoque
            Estoque.ItemEstoque item1 = new Estoque.ItemEstoque("P002", "Filtro de Óleo", 20, 45.0, "Filtro de óleo para motor");
            Estoque.ItemEstoque item2 = new Estoque.ItemEstoque("P003", "Óleo Motor", 30, 35.0, "Óleo sintético 5W30");
            
            oficina.getEstoque().adicionarItem(item1);
            oficina.getEstoque().adicionarItem(item2);
            
            System.out.println("Itens adicionados ao estoque:");
            oficina.getEstoque().listarItens();

            // Atualizar quantidade
            oficina.getEstoque().atualizarQuantidade("P002", 15);
            System.out.println("\nQuantidade atualizada:");
            oficina.getEstoque().listarItens();

            // Receber de fornecedores
            System.out.println("\nRecebendo itens do fornecedor:");
            oficina.registrarCompraPecas();
            
            // Criar ordens de serviço de demonstração diretamente
        OrdemDeServicoBuilder builder = new OrdemServicoConcretaBuilder();
        OrdemServicoDirector director = new OrdemServicoDirector(builder);

        // OS001: Troca de óleo e revisão geral
        List<Estoque.ItemEstoque> itensOS1 = new ArrayList<>();
        itensOS1.add(oficina.getEstoque().getItens().get(1)); // Filtro de Óleo (F001)
        itensOS1.add(oficina.getEstoque().getItens().get(2)); // Óleo Motor (O001)
        director.construirOrdemCompleta(
            "Entrada",
            "Troca de óleo e revisão geral",
            "08/05/2024",
            "16:00",
            "Nome funcionario3",
            "Nome cliente3",
            "Concluído",
            itensOS1,
            new ArrayList<>()
        );
        OrdemServico os1 = builder.build();
        oficina.getOrdensServico().add(os1);
        oficina.getCaixa().registrarEntrada(os1);

        // OS002: Reparo elétrico e troca de bateria
        List<Estoque.ItemEstoque> itensOS2 = new ArrayList<>();
        itensOS2.add(oficina.getEstoque().getItens().get(4)); // Bateria (B001)
        director.construirOrdemCompleta(
            "Entrada",
            "Reparo elétrico e troca de bateria",
            "09/05/2024",
            "10:00",
            "Nome funcionario2",
            "Nome cliente2",
            "Concluído",
            itensOS2,
            new ArrayList<>()
        );
        OrdemServico os2 = builder.build();
        oficina.getOrdensServico().add(os2);
        oficina.getCaixa().registrarEntrada(os2);

        // OS003: Manutenção de suspensão
        List<Estoque.ItemEstoque> itensOS3 = new ArrayList<>();
        itensOS3.add(oficina.getEstoque().getItens().get(6)); // Amortecedor (A001)
        director.construirOrdemCompleta(
            "Entrada",
            "Manutenção de suspensão e alinhamento",
            "09/05/2024",
            "14:00",
            "Nome funcionario",
            "Nome cliente",
            "Concluído",
            itensOS3,
            new ArrayList<>()
        );
        OrdemServico os3 = builder.build();
        oficina.getOrdensServico().add(os3);
        oficina.getCaixa().registrarEntrada(os3);
        }

        // Questão 10: Extrato
        {
            System.out.println("\n=== QUESTÃO 10: EXTRATO ===");
            System.out.println("\nTestando geração de extrato:");
            
            oficina.listarTodasTransacoes();
        }

        // Questão 11: Variáveis Estáticas
        {
            System.out.println("\n=== QUESTÃO 11: VARIÁVEIS ESTÁTICAS ===");
            System.out.println("\nTestando variáveis estáticas:");
            System.out.println("Valor do contador de veículos: " + oficina.getContadorVeiculosPrivate());
        }

        // Questão 12: Método de Classe
        {
            System.out.println("\n=== QUESTÃO 12: MÉTODO DE CLASSE ===");
            System.out.println("\nTestando método de classe:");
            System.out.println("Valor do método de classe: " + SistemaOficinac.getContadorVeiculosPrivate());
        }

        // Questão 13: Comparator
        {
            System.out.println("\n=== QUESTÃO 13: COMPARATOR ===");
            System.out.println("\nTestando comparadores:");
            
            List<Cliente> clientes = new ArrayList<>();
            clientes.add(new Cliente("Ana", "11877777777", "Rua N, 1111", "111.222.333-44"));
            clientes.add(new Cliente("Bruno", "11866666666", "Rua O, 1212", "222.333.444-55"));
            clientes.add(new Cliente("Carla", "11855555555", "Rua P, 1313", "333.444.555-66"));
            
            // Ordenar por nome
            System.out.println("\nOrdenando por nome:");
            Collections.sort(clientes, new Comparadores.ClientePorNome());
            clientes.forEach(System.out::println);
            
            // Ordenar por quantidade de veículos
            System.out.println("\nOrdenando por quantidade de veículos:");
            Collections.sort(clientes, new Comparadores.ClientePorQtdVeiculos());
            clientes.forEach(System.out::println);
            
            // Testar busca binária
            System.out.println("\nTestando busca binária:");
            Cliente clienteBusca = new Cliente("Bruno", "", "", "");
            int indice = Collections.binarySearch(clientes, clienteBusca, new Comparadores.ClientePorNome());
            if (indice >= 0) {
                System.out.println("Cliente encontrado na posição " + indice + ": " + clientes.get(indice));
            } else {
                System.out.println("Cliente não encontrado");
            }
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
                
                // Verificar dados carregados
                System.out.println("\nDados carregados:");
                System.out.println("Clientes: " + oficina.getClientes().size());
                System.out.println("Funcionários: " + oficina.getFuncionarios().size());
                System.out.println("Ordens de serviço: " + oficina.getOrdensServico().size());
                System.out.println("Itens no estoque: " + oficina.getEstoque().getItens().size());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao manipular dados: " + e.getMessage());
            }
        }


        // Questão 15: Iterator e forEach
        {
            System.out.println("\n=== QUESTÃO 15: ITERATOR E FOREACH ===");
            System.out.println("\nTestando Iterator e forEach em diferentes coleções:");
            
            // Teste com lista de clientes
            System.out.println("\nIterando sobre clientes usando Iterator:");
            List<Cliente> clientes = oficina.getClientes();
            Iterator<Cliente> iteratorClientes = clientes.iterator();
            while (iteratorClientes.hasNext()) {
                System.out.println(iteratorClientes.next());
            }
            
            // Teste com lista de funcionários
            System.out.println("\nIterando sobre funcionários usando Iterator:");
            List<Funcionario> funcionarios = oficina.getFuncionarios();
            Iterator<Funcionario> iteratorFuncionarios = funcionarios.iterator();
            while (iteratorFuncionarios.hasNext()) {
                System.out.println(iteratorFuncionarios.next());
            }
            
            // Teste com lista de ordens de serviço
            System.out.println("\nIterando sobre ordens de serviço usando Iterator:");
            List<OrdemServico> ordens = oficina.getOrdensServico();
            Iterator<OrdemServico> iteratorOrdens = ordens.iterator();
            while (iteratorOrdens.hasNext()) {
                System.out.println(iteratorOrdens.next());
            }
            
            // Teste do forEach
            System.out.println("\nUsando forEach em clientes:");
            clientes.forEach(System.out::println);
            
            // Explicar relação entre Iterator e forEach
            System.out.println("\nRelação entre Iterator e forEach:");
            System.out.println("O forEach é uma forma mais moderna e concisa de iterar sobre coleções,");
            System.out.println("internamente ele utiliza um Iterator para percorrer os elementos.");
            System.out.println("A principal diferença é que o forEach é mais legível e menos propenso a erros,");
            System.out.println("enquanto o Iterator oferece mais controle sobre a iteração.");
        }

        // Questão 16: Comparator e Sort
        {
            System.out.println("\n=== QUESTÃO 16: COMPARATOR E SORT ===");
            System.out.println("\nTestando diferentes comparadores e ordenação:");
            
            // Criar lista de clientes para teste
            List<Cliente> clientes = new ArrayList<>();
            clientes.add(new Cliente("Zé", "11999999999", "Rua A, 1", "111.111.111-11"));
            clientes.add(new Cliente("Ana", "11988888888", "Rua B, 2", "222.222.222-22"));
            clientes.add(new Cliente("Pedro", "11977777777", "Rua C, 3", "333.333.333-33"));
            
            // Adicionar veículos para alguns clientes
            clientes.get(0).adicionarVeiculo(new Veiculo("Civic", "ABC1234", 2020, "Honda", "Preto"));
            clientes.get(0).adicionarVeiculo(new Veiculo("Corolla", "DEF5678", 2021, "Toyota", "Prata"));
            clientes.get(1).adicionarVeiculo(new Veiculo("HB20", "GHI9012", 2022, "Hyundai", "Branco"));
            
            // Primeira ordenação: por nome
            System.out.println("\nOrdenando por nome:");
            Collections.sort(clientes, new Comparadores.ClientePorNome());
            clientes.forEach(c -> System.out.println(c.getNome()));
            
            // Segunda ordenação: por quantidade de veículos
            System.out.println("\nOrdenando por quantidade de veículos:");
            Collections.sort(clientes, new Comparadores.ClientePorQtdVeiculos());
            clientes.forEach(c -> System.out.println(c.getNome() + " - " + c.getVeiculos().size() + " veículos"));
        }

        // Questão 17: Find e BinarySearch
        {
            System.out.println("\n=== QUESTÃO 17: FIND E BINARYSEARCH ===");
            System.out.println("\nTestando método find e binarySearch:");
            
            // Criar lista de clientes para teste
            List<Cliente> clientes = new ArrayList<>();
            clientes.add(new Cliente("Ana", "11999999999", "Rua A, 1", "111.111.111-11"));
            clientes.add(new Cliente("Bruno", "11988888888", "Rua B, 2", "222.222.222-22"));
            clientes.add(new Cliente("Carlos", "11977777777", "Rua C, 3", "333.333.333-33"));
            clientes.add(new Cliente("Daniel", "11966666666", "Rua D, 4", "444.444.444-44"));
            
            // Ordenar lista para busca binária
            Collections.sort(clientes, new Comparadores.ClientePorNome());
            
            // Testar busca binária
            System.out.println("\nTestando binarySearch:");
            Cliente clienteBusca = new Cliente("Bruno", "", "", "");
            int indiceBinario = Collections.binarySearch(clientes, clienteBusca, new Comparadores.ClientePorNome());
            if (indiceBinario >= 0) {
                System.out.println("Cliente encontrado na posição " + indiceBinario + ": " + clientes.get(indiceBinario));
            } else {
                System.out.println("Cliente não encontrado");
            }
            
            // Testar método find com Iterator
            System.out.println("\nTestando find com Iterator:");
            Cliente clienteEncontrado = findCliente(clientes, "Bruno");
            if (clienteEncontrado != null) {
                System.out.println("Cliente encontrado: " + clienteEncontrado);
            } else {
                System.out.println("Cliente não encontrado");
            }
            
            // Comparar resultados
            System.out.println("\nComparação dos métodos:");
            System.out.println("binarySearch: " + (indiceBinario >= 0 ? "encontrado" : "não encontrado"));
            System.out.println("find: " + (clienteEncontrado != null ? "encontrado" : "não encontrado"));
        }

        // Questão 18: Fluxo Completo 10 Clientes
        {
            System.out.println("\n=== QUESTÃO 18: FLUXO COMPLETO 10 CLIENTES ===");
            System.out.println("\nTestando fluxo completo de atendimento para 10 clientes:");
            
            // Criar lista de serviços disponíveis
            List<Servico> servicosDisponiveis = new ArrayList<>();
            servicosDisponiveis.add(new Servico("Troca de óleo", "Troca de óleo e filtro", 150.0, 60));
            servicosDisponiveis.add(new Servico("Alinhamento", "Alinhamento e balanceamento", 200.0, 90));
            servicosDisponiveis.add(new Servico("Revisão", "Revisão completa", 300.0, 120));
            
            // Criar lista de itens no estoque
            List<Estoque.ItemEstoque> itensEstoque = new ArrayList<>();
            itensEstoque.add(new Estoque.ItemEstoque("P001", "Óleo Motor", 50, 35.0, "Óleo sintético 5W30"));
            itensEstoque.add(new Estoque.ItemEstoque("P002", "Filtro de Óleo", 30, 25.0, "Filtro de óleo"));
            itensEstoque.add(new Estoque.ItemEstoque("P003", "Pastilha de Freio", 40, 80.0, "Pastilha de freio"));
            
            // Adicionar itens ao estoque
            for (Estoque.ItemEstoque item : itensEstoque) {
                oficina.getEstoque().adicionarItem(item);
            }
            
            // Criar lista de mecânicos
            List<Funcionario> mecanicos = new ArrayList<>();
            mecanicos.add(new Funcionario("João", "11955555555", "Rua X, 1", "Mecânico", 3000.0, "MEC001", "Mecânica"));
            mecanicos.add(new Funcionario("Maria", "11944444444", "Rua Y, 2", "Mecânico", 3200.0, "MEC002", "Mecânica"));
            
            // Processar 10 clientes
            for (int i = 1; i <= 10; i++) {
                System.out.println("\n=== Atendimento do Cliente " + i + " ===");
                
                // 1. Cadastro do Cliente
                Cliente cliente = new Cliente("Cliente " + i, "119" + String.format("%08d", i), 
                    "Rua " + i + ", " + i, String.format("%03d.%03d.%03d-%02d", i, i, i, i));
                oficina.getClientes().add(cliente);
                System.out.println("1. Cliente cadastrado: " + cliente);
                
                // 2. Cadastro do Veículo
                Veiculo veiculo = new Veiculo("Modelo " + i, "ABC" + String.format("%04d", i), 
                    2020 + (i % 5), "Marca " + i, "Cor " + i);
                cliente.adicionarVeiculo(veiculo);
                System.out.println("2. Veículo cadastrado: " + veiculo);
                
                // 3. Criação da Ordem de Serviço
                List<Servico> servicos = new ArrayList<>();
                servicos.add(servicosDisponiveis.get(i % servicosDisponiveis.size()));
                
                OrdemServico ordem = new OrdemServico(cliente, veiculo, servicos, 
                    mecanicos.get(i % mecanicos.size()), "2024-03-" + String.format("%02d", i), 
                    String.format("%02d:00", 9 + (i % 8)));
                oficina.getOrdensServico().add(ordem);
                System.out.println("3. Ordem de serviço criada: " + ordem);
                
                // 4. Registro de Baixa no Estoque
                Estoque.ItemEstoque item = itensEstoque.get(i % itensEstoque.size());
                ordem.getItensUtilizados().add(item);
                System.out.println("4. Item registrado: " + item);
                
                // 5. Conclusão do Serviço
                ordem.setStatus("Concluído");
                double valorTotal = ordem.getServicos().stream().mapToDouble(Servico::getValor).sum() +
                    ordem.getItensUtilizados().stream().mapToDouble(estoqueItem -> estoqueItem.getPreco() * estoqueItem.getQuantidade()).sum();
                ordem.setValor(valorTotal);
                System.out.println("5. Serviço concluído. Valor total: R$ " + ordem.getValor());
                
                // 6. Emissão da Nota Fiscal
                System.out.println("\nNota Fiscal #" + ordem.getNumeroOrdem());
                System.out.println("Cliente: " + ordem.getCliente().getNome());
                System.out.println("Veículo: " + ordem.getVeiculo().getModelo() + " - " + ordem.getVeiculo().getPlaca());
                System.out.println("Serviços realizados:");
                ordem.getServicos().forEach(s -> System.out.println("- " + s.getNome() + ": R$ " + s.getValor()));
                System.out.println("Itens utilizados:");
                ordem.getItensUtilizados().forEach(estoqueItem -> System.out.println("- " + estoqueItem.getNome() + ": R$ " + estoqueItem.getPreco()));
                System.out.println("Valor total: R$ " + ordem.getValor());
            }
        }
    }

    private static Cliente findCliente(List<Cliente> clientes, String nome) {
        Iterator<Cliente> iterator = clientes.iterator();
        while (iterator.hasNext()) {
            Cliente cliente = iterator.next();
            if (cliente.getNome().equals(nome)) {
                return cliente;
            }
        }
        return null;
    }
} 