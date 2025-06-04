package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que gerencia o caixa da oficina, incluindo entradas, saídas e saldo.
 */
public class Caixa {
    private double saldo; // Saldo atual do caixa
    private List<OrdemServico> ordensServico; // Lista de ordens de serviço

    public Caixa() {
        this.saldo = 0;
        this.ordensServico = new ArrayList<>();
    }

    // Métodos para manipular ordens de serviço
    public List<OrdemServico> getOrdensServico() {
        return new ArrayList<>(ordensServico);
    }

    // Método atualizado para registrar entrada com agendamento e itens
    public void registrarEntrada(String descricao, String data, String categoria, 
                               String responsavel, String cliente, Agenda.Agendamento agendamento,
                               List<Estoque.ItemEstoque> itens) {
        OrdemServico os = new OrdemServico(
            "Entrada", 
            descricao, 
            data, 
            categoria, 
            responsavel, 
            cliente, 
            agendamento
        );
        
        if (itens != null) {
            for (Estoque.ItemEstoque item : itens) {
                os.adicionarItemEstoque(item, item.getQuantidade());
            }
        }
        ordensServico.add(os);
        saldo += os.getValor();
        System.out.println("Entrada registrada: " + os);
    }

    // Método atualizado para registrar saída com agendamento e itens
    public void registrarSaida(String descricao, String data, String categoria, 
                             String responsavel, String cliente, Agenda.Agendamento agendamento,
                             List<Estoque.ItemEstoque> itens) {
        OrdemServico os = new OrdemServico(
            "Saída", 
            descricao, 
            data, 
            categoria, 
            responsavel, 
            cliente, 
            agendamento
        );
        
        if (itens != null) {
            for (Estoque.ItemEstoque item : itens) {
                os.adicionarItemEstoque(item, item.getQuantidade());
            }
        }
        ordensServico.add(os);
        saldo -= os.getValor();
        System.out.println("Saída registrada: " + os);
    }

    // Método simplificado para registrar entrada
    public void registrarEntrada(double valor, String descricao, String data, 
                               String categoria, String responsavel, Agenda.Agendamento agendamento) {
        OrdemServico os = new OrdemServico(
            "Entrada", 
            descricao, 
            data, 
            categoria, 
            responsavel, 
            null,  // cliente não informado
            agendamento
        );
        ordensServico.add(os);
        saldo += valor;
        System.out.println("Entrada registrada: " + os);
    }

    // Método simplificado para registrar saída
    public void registrarSaida(double valor, String descricao, String data, 
                             String categoria, String responsavel, Agenda.Agendamento agendamento) {
        OrdemServico os = new OrdemServico(
            "Saída", 
            descricao, 
            data, 
            categoria, 
            responsavel, 
            null,  // cliente não informado
            agendamento
        );
        ordensServico.add(os);
        saldo -= valor;
        System.out.println("Saída registrada: " + os);
    }

    // Métodos de filtro (mantidos iguais)
    public List<OrdemServico> getOrdensPorPeriodo(String dataInicio, String dataFim) {
        return ordensServico.stream()
                .filter(t -> t.getData().compareTo(dataInicio) >= 0 && t.getData().compareTo(dataFim) <= 0)
                .collect(Collectors.toList());
    }

    public List<OrdemServico> getOrdensPorCategoria(String categoria) {
        return ordensServico.stream()
                .filter(t -> t.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    // Métodos de relatório (mantidos iguais)
    public void gerarRelatorioDiario(String data) {
        System.out.println("\n=== RELATÓRIO DIÁRIO (" + data + ") ===");
        ordensServico.stream()
            .filter(t -> t.getData().equals(data))
            .forEach(System.out::println);
        
        System.out.println("Total de entradas: R$" + getTotalEntradas(data));
        System.out.println("Total de saídas: R$" + getTotalSaidas(data));
        System.out.println("Saldo do dia: R$" + (getTotalEntradas(data) - getTotalSaidas(data)));
    }

    private double getTotalEntradas(String data) {
        return ordensServico.stream()
                .filter(t -> t.getTipo().equals("Entrada") && t.getData().equals(data))
                .mapToDouble(OrdemServico::getValor)
                .sum();
    }

    private double getTotalSaidas(String data) {
        return ordensServico.stream()
                .filter(t -> t.getTipo().equals("Saída") && t.getData().equals(data))
                .mapToDouble(OrdemServico::getValor)
                .sum();
    }

    public double getSaldo() {
        return saldo;
    }

    public void gerarRelatorioMensal(int mes, int ano) {
        System.out.println("\n=== RELATÓRIO MENSAL (" + mes + "/" + ano + ") ===");
        
        double totalEntradas = 0;
        double totalSalarios = 0;
        double totalPecas = 0;
        double outrasDespesas = 0;
        
        for (OrdemServico ordem : ordensServico) {
            String[] dataParts = ordem.getData().split("/");
            int ordemMes = Integer.parseInt(dataParts[1]);
            int ordemAno = Integer.parseInt(dataParts[2]);
            
            if (ordemMes == mes && ordemAno == ano) {
                System.out.println(ordem);
                
                if (ordem.getTipo().equals("Entrada")) {
                    totalEntradas += ordem.getValor();
                } else {
                    if (ordem.getDescricao().contains("Pagamento de salários")) {
                        totalSalarios += ordem.getValor();
                    } else if (ordem.getDescricao().contains("Compra de peças")) {
                        totalPecas += ordem.getValor();
                    } else {
                        outrasDespesas += ordem.getValor();
                    }
                }
            }
        }
        
        System.out.println("\nRESUMO DO MÊS:");
        System.out.println("Total de entradas: R$ " + totalEntradas);
        System.out.println("Total de despesas com salários: R$ " + totalSalarios);
        System.out.println("Total de despesas com peças: R$ " + totalPecas);
        System.out.println("Outras despesas: R$ " + outrasDespesas);
        System.out.println("Saldo do mês: R$ " + (totalEntradas - (totalSalarios + totalPecas + outrasDespesas)));
    }
}