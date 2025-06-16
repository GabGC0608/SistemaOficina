package com.mycompany.sistemaoficinac;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe que gerencia o caixa da oficina, incluindo entradas, saídas e saldo.
 */
public class Caixa {
    private double saldo; // Saldo atual do caixa
    private List<OrdemServico> ordensServico; // Lista de ordens de serviço

    public Caixa() {
        this.saldo = 0.0;
        this.ordensServico = new ArrayList<>();
    }

    // Métodos para manipular ordens de serviço
    public List<OrdemServico> getOrdensServico() {
        return new ArrayList<>(ordensServico);
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    // Método simplificado para registrar entrada
    public void registrarEntrada(OrdemServico os) {
        if (os != null) {
            ordensServico.add(os);
            saldo += os.getValor();
        }
    }

    // Método atualizado para registrar saída com agendamento e itens
    /**
     * Registra uma saída no caixa.
     * @param valor Valor da saída
     * @param descricao Descrição da saída
     * @param data Data da saída
     * @param categoria Categoria da saída (ex: "Despesa", "Compra de peças", etc)
     * @param responsavel Responsável pela saída
     * @param itens Lista de itens de estoque envolvidos (pode ser null)
     */
    public void registrarSaida(double valor, String descricao, String data, String categoria,
                             String responsavel, List<Estoque.ItemEstoque> itens) {
        OrdemServico os = new OrdemServico(
            "Saída",
            valor,
            descricao,
            data,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
            categoria,
            responsavel,
            null,
            new ArrayList<>(),
            itens
        );
        ordensServico.add(os);
        saldo -= valor;
    }

    // Métodos de filtro (mantidos iguais)
    public List<OrdemServico> getOrdensPorPeriodo(String dataInicio, String dataFim) {
        return ordensServico.stream()
            .filter(os -> os.getData().compareTo(dataInicio) >= 0 && 
                         os.getData().compareTo(dataFim) <= 0)
            .collect(Collectors.toList());
    }

    public List<OrdemServico> getOrdensPorCategoria(String categoria) {
        return ordensServico.stream()
            .filter(os -> os.getCategoria() != null && 
                         os.getCategoria().equalsIgnoreCase(categoria))
            .collect(Collectors.toList());
    }

    // Métodos de relatório (mantidos iguais)
    public void gerarRelatorioDiario(String data) {
        double totalEntradas = getTotalEntradas(data);
        double totalSaidas = getTotalSaidas(data);
        double saldoDiario = totalEntradas - totalSaidas;

        System.out.println("\n=== Relatório Diário ===");
        System.out.println("Data: " + data);
        System.out.printf("Total de Entradas: R$ %.2f%n", totalEntradas);
        System.out.printf("Total de Saídas: R$ %.2f%n", totalSaidas);
        System.out.printf("Saldo do Dia: R$ %.2f%n", saldoDiario);
        System.out.println("======================");
    }

    private double getTotalEntradas(String data) {
        return ordensServico.stream()
            .filter(os -> os.getData().equals(data) && os.getTipo().equals("Entrada"))
            .mapToDouble(OrdemServico::getValor)
            .sum();
    }

    private double getTotalSaidas(String data) {
        return ordensServico.stream()
            .filter(os -> os.getData().equals(data) && os.getTipo().equals("Saída"))
            .mapToDouble(OrdemServico::getValor)
            .sum();
    }

    public double getSaldo() {
        return saldo;
    }

    public void gerarRelatorioMensal(int mes, int ano) {
        String mesStr = String.format("%02d", mes);
        String dataInicio = "01/" + mesStr + "/" + ano;
        String dataFim = "31/" + mesStr + "/" + ano;

        List<OrdemServico> ordensMes = getOrdensPorPeriodo(dataInicio, dataFim);
        double totalEntradas = ordensMes.stream()
            .filter(os -> os.getTipo().equals("Entrada"))
            .mapToDouble(OrdemServico::getValor)
            .sum();
        double totalSaidas = ordensMes.stream()
            .filter(os -> os.getTipo().equals("Saída"))
            .mapToDouble(OrdemServico::getValor)
            .sum();

        System.out.println("\n=== Relatório Mensal ===");
        System.out.printf("Mês/Ano: %02d/%d%n", mes, ano);
        System.out.printf("Total de Entradas: R$ %.2f%n", totalEntradas);
        System.out.printf("Total de Saídas: R$ %.2f%n", totalSaidas);
        System.out.printf("Saldo do Mês: R$ %.2f%n", totalEntradas - totalSaidas);
        System.out.println("======================");
    }

    /**
     * Gera um balanço mensal detalhado com análise financeira.
     * Inclui informações como lucro/prejuízo, margem de lucro, e análise de despesas por categoria.
     * 
     * @param mes Mês do balanço (1-12)
     * @param ano Ano do balanço
     */
    public void gerarBalancoMensal(int mes, int ano) {
        String mesStr = String.format("%02d", mes);
        String dataInicio = "01/" + mesStr + "/" + ano;
        String dataFim = "31/" + mesStr + "/" + ano;

        List<OrdemServico> ordensMes = getOrdensPorPeriodo(dataInicio, dataFim);
        
        // Agrupa por categoria
        var entradasPorCategoria = ordensMes.stream()
            .filter(os -> os.getTipo().equals("Entrada"))
            .collect(Collectors.groupingBy(
                OrdemServico::getCategoria,
                Collectors.summingDouble(OrdemServico::getValor)
            ));

        var saidasPorCategoria = ordensMes.stream()
            .filter(os -> os.getTipo().equals("Saída"))
            .collect(Collectors.groupingBy(
                OrdemServico::getCategoria,
                Collectors.summingDouble(OrdemServico::getValor)
            ));

        System.out.println("\n=== Balanço Mensal ===");
        System.out.printf("Mês/Ano: %02d/%d%n", mes, ano);
        
        System.out.println("\nEntradas por Categoria:");
        entradasPorCategoria.forEach((categoria, valor) ->
            System.out.printf("%s: R$ %.2f%n", categoria, valor));

        System.out.println("\nSaídas por Categoria:");
        saidasPorCategoria.forEach((categoria, valor) ->
            System.out.printf("%s: R$ %.2f%n", categoria, valor));

        double totalEntradas = entradasPorCategoria.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalSaidas = saidasPorCategoria.values().stream().mapToDouble(Double::doubleValue).sum();

        System.out.println("\nResumo:");
        System.out.printf("Total de Entradas: R$ %.2f%n", totalEntradas);
        System.out.printf("Total de Saídas: R$ %.2f%n", totalSaidas);
        System.out.printf("Resultado Líquido: R$ %.2f%n", totalEntradas - totalSaidas);
        System.out.println("======================");
    }
}