package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que representa uma ordem de serviço na oficina.
 * Contém informações sobre o serviço, cliente, itens utilizados e valores.
 */
public class OrdemServico {
    @JsonProperty("tipo")
    private String tipo;
    @JsonProperty("valor")
    private double valor;
    @JsonProperty("descricao")
    private String descricao;
    @JsonProperty("data")
    private String data;
    @JsonProperty("categoria")
    private String categoria;
    @JsonProperty("responsavel")
    private String responsavel;
    @JsonProperty("cliente")
    private String cliente;
    @JsonProperty("agendamento")
    private Agenda.Agendamento agendamento;
    @JsonProperty("itensUtilizados")
    private List<Estoque.ItemEstoque> itensUtilizados;
    @JsonProperty("notaFiscalGerada")
    private boolean notaFiscalGerada;

    // Construtor principal
    public OrdemServico(String tipo, String descricao, String data, String categoria,
                       String responsavel, String cliente, Agenda.Agendamento agendamento) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.data = data;
        this.categoria = categoria;
        this.responsavel = responsavel;
        this.cliente = cliente;
        this.agendamento = agendamento;
        this.itensUtilizados = new ArrayList<>();
        this.notaFiscalGerada = false;
        this.valor = calcularValorTotal();
    }

    // Construtor vazio para serialização
    public OrdemServico() {
        this.itensUtilizados = new ArrayList<>();
    }

    @JsonCreator
    public OrdemServico(
        @JsonProperty("tipo") String tipo,
        @JsonProperty("valor") double valor,
        @JsonProperty("descricao") String descricao,
        @JsonProperty("data") String data,
        @JsonProperty("categoria") String categoria,
        @JsonProperty("responsavel") String responsavel,
        @JsonProperty("cliente") String cliente,
        @JsonProperty("agendamento") Agenda.Agendamento agendamento,
        @JsonProperty("itensUtilizados") List<Estoque.ItemEstoque> itensUtilizados,
        @JsonProperty("notaFiscalGerada") boolean notaFiscalGerada) {

        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
        this.categoria = categoria;
        this.responsavel = responsavel;
        this.cliente = cliente;
        this.agendamento = agendamento;
        this.itensUtilizados = (itensUtilizados != null) ? itensUtilizados : new ArrayList<>();
        this.notaFiscalGerada = notaFiscalGerada;
    }

    // Getters e Setters
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    
    public Agenda.Agendamento getAgendamento() { return agendamento; }
    public void setAgendamento(Agenda.Agendamento agendamento) { 
        this.agendamento = agendamento; 
        this.valor = calcularValorTotal();
    }

    public void setItensUtilizados(List<Estoque.ItemEstoque> itensUtilizados) {
        this.itensUtilizados = itensUtilizados;
        this.valor = calcularValorTotal();
    }
    
    
    public List<Estoque.ItemEstoque> getItensUtilizados() { 
        return new ArrayList<>(itensUtilizados); 
    }
    
    public boolean isNotaFiscalGerada() { return notaFiscalGerada; }
    public void setNotaFiscalGerada(boolean notaFiscalGerada) {
        this.notaFiscalGerada = notaFiscalGerada;
    }

    /**
     * Adiciona um item do estoque à ordem de serviço
     * @param item Item do estoque a ser adicionado
     * @param quantidade Quantidade utilizada
     */
    public void adicionarItemEstoque(Estoque.ItemEstoque item, int quantidade) {
        if (quantidade <= item.getQuantidade()) {
            Estoque.ItemEstoque itemUtilizado = new Estoque.ItemEstoque(
                item.getCodigo(),
                item.getNome(),
                quantidade,
                item.getPreco(),
                item.getDescricao()
            );
            itensUtilizados.add(itemUtilizado);
            this.valor = calcularValorTotal(); // Recalcula o valor total
        } else {
            throw new IllegalArgumentException("Quantidade insuficiente em estoque");
        }
    }

    /**
     * Calcula o valor total dos itens utilizados
     * @return Valor total dos itens
     */
    private double getValorItens() {
        return itensUtilizados.stream()
            .mapToDouble(item -> item.getPreco() * item.getQuantidade())
            .sum();
    }

    /**
     * Calcula o valor total da ordem de serviço (serviços do agendamento + itens)
     * @return Valor total da ordem de serviço
     */
    private double calcularValorTotal() {
        double valorServicos = 0;
        if (agendamento != null) {
            valorServicos = agendamento.getValorTotal();
        }
        double valorItens = getValorItens();
        return valorServicos + valorItens;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s | %s | R$ %.2f", tipo, data, valor));
        
        if (categoria != null) sb.append(" | " + categoria);
        if (responsavel != null) sb.append(" | Resp: " + responsavel);
        if (cliente != null) sb.append(" | Cliente: " + cliente);
        
        if (agendamento != null) {
            sb.append("\n  Serviços: ").append(agendamento.getServicos().stream()
                .map(s -> s.getNome() + " (R$ " + s.getValor() + ")")
                .collect(Collectors.joining(", ")));
        }
        
        if (!itensUtilizados.isEmpty()) {
            sb.append("\n  Peças: ").append(itensUtilizados.stream()
                .map(i -> i.getNome() + " x" + i.getQuantidade() + " (R$ " + i.getPreco() * i.getQuantidade() + ")")
                .collect(Collectors.joining(", ")));
        }
        
        if (descricao != null) sb.append("\n  Obs: ").append(descricao);
        
        if (notaFiscalGerada) sb.append("\n  [NOTA FISCAL GERADA]");
        
        return sb.toString();
    }
}