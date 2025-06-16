package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

/**
 * Classe que representa uma ordem de serviço na oficina.
 * Contém informações sobre o serviço, cliente, itens utilizados e valores.
 */
public class OrdemServico {
    @JsonProperty("numeroOrdem")
    private String numeroOrdem;
    @JsonProperty("cliente")
    private Cliente cliente;
    @JsonProperty("veiculo")
    private Veiculo veiculo;
    @JsonProperty("servicos")
    private List<Servico> servicos;
    @JsonProperty("responsavel")
    private Funcionario responsavel;
    @JsonProperty("data")
    private String data;
    @JsonProperty("horario")
    private String horario;
    @JsonProperty("status")
    private String status;
    @JsonProperty("elevadorAlocado")
    private Elevador elevadorAlocado;
    @JsonProperty("itensUtilizados")
    private List<Estoque.ItemEstoque> itensUtilizados;
    @JsonProperty("tipo")
    private String tipo;
    @JsonProperty("categoria")
    private String categoria;
    @JsonProperty("descricao")
    private String descricao;
    @JsonProperty("valor")
    private double valor;

    /**
     * Construtor da classe OrdemServico.
     *
     * @param cliente Cliente que solicitou o serviço
     * @param veiculo Veículo a ser atendido
     * @param servicos Lista de serviços a serem realizados
     * @param responsavel Funcionário responsável pelo serviço
     * @param data Data do serviço
     * @param horario Horário do serviço
     */
    public OrdemServico(Cliente cliente, Veiculo veiculo, List<Servico> servicos,
                       Funcionario responsavel, String data, String horario) {
        this.numeroOrdem = UUID.randomUUID().toString().substring(0, 8);
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.servicos = servicos;
        this.responsavel = responsavel;
        this.data = data;
        this.horario = horario;
        this.status = "Agendado";
        this.tipo = "Entrada";
        this.categoria = "Serviços";
        this.itensUtilizados = new ArrayList<>();
        this.valor = calcularValorTotal();
    }

    /**
     * Construtor para transações financeiras.
     */
    public OrdemServico(String tipo, double valor, String descricao, String data, 
                       String horario, String categoria, String responsavel, 
                       String cliente, List<Servico> servicos, 
                       List<Estoque.ItemEstoque> itens) {
        this.numeroOrdem = UUID.randomUUID().toString().substring(0, 8);
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
        this.horario = horario;
        this.categoria = categoria;
        this.status = "Pendente";
        this.servicos = servicos != null ? servicos : new ArrayList<>();
        this.itensUtilizados = itens != null ? itens : new ArrayList<>();
    }

    // Construtor vazio para serialização
    public OrdemServico() {
        this.itensUtilizados = new ArrayList<>();
        this.servicos = new ArrayList<>();
    }

    @JsonCreator
    public OrdemServico(
        @JsonProperty("numeroOrdem") String numeroOrdem,
        @JsonProperty("cliente") Cliente cliente,
        @JsonProperty("veiculo") Veiculo veiculo,
        @JsonProperty("servicos") List<Servico> servicos,
        @JsonProperty("responsavel") Funcionario responsavel,
        @JsonProperty("data") String data,
        @JsonProperty("horario") String horario,
        @JsonProperty("status") String status,
        @JsonProperty("elevadorAlocado") Elevador elevadorAlocado,
        @JsonProperty("itensUtilizados") List<Estoque.ItemEstoque> itensUtilizados,
        @JsonProperty("tipo") String tipo,
        @JsonProperty("categoria") String categoria,
        @JsonProperty("descricao") String descricao,
        @JsonProperty("valor") double valor
    ) {
        this.numeroOrdem = numeroOrdem;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.servicos = servicos;
        this.responsavel = responsavel;
        this.data = data;
        this.horario = horario;
        this.status = status;
        this.elevadorAlocado = elevadorAlocado;
        this.itensUtilizados = itensUtilizados;
        this.tipo = tipo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
    }

    /**
     * Obtém o número da ordem de serviço.
     *
     * @return Número da ordem
     */
    public String getNumeroOrdem() {
        return numeroOrdem;
    }

    /**
     * Obtém o cliente da ordem de serviço.
     *
     * @return Cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Obtém o veículo da ordem de serviço.
     *
     * @return Veículo
     */
    public Veiculo getVeiculo() {
        return veiculo;
    }

    /**
     * Obtém a lista de serviços da ordem.
     *
     * @return Lista de serviços
     */
    public List<Servico> getServicos() {
        return new ArrayList<>(servicos);
    }

    /**
     * Obtém o funcionário responsável.
     *
     * @return Funcionário responsável
     */
    public Funcionario getResponsavel() {
        return responsavel;
    }

    /**
     * Obtém a data do serviço.
     *
     * @return Data do serviço
     */
    public String getData() {
        return data;
    }

    /**
     * Obtém o horário do serviço.
     *
     * @return Horário do serviço
     */
    public String getHorario() {
        return horario;
    }

    /**
     * Obtém o status da ordem de serviço.
     *
     * @return Status da ordem
     */
    public String getStatus() {
        return status;
    }

    /**
     * Define o status da ordem de serviço.
     *
     * @param status Novo status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Obtém o elevador alocado para esta ordem.
     *
     * @return Elevador alocado ou null se nenhum estiver alocado
     */
    public Elevador getElevadorAlocado() {
        return elevadorAlocado;
    }

    /**
     * Define o elevador alocado para esta ordem.
     *
     * @param elevadorAlocado Elevador a ser alocado
     */
    public void setElevadorAlocado(Elevador elevadorAlocado) {
        this.elevadorAlocado = elevadorAlocado;
    }

    public void setItensUtilizados(List<Estoque.ItemEstoque> itensUtilizados) {
        this.itensUtilizados = itensUtilizados;
    }

    public List<Estoque.ItemEstoque> getItensUtilizados() {
        return new ArrayList<>(itensUtilizados);
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
        } else {
            throw new IllegalArgumentException("Quantidade insuficiente em estoque");
        }
    }

    public void adicionarServico(Servico servico) {
        if (servico != null) {
            servicos.add(servico);
        } else {
            throw new IllegalArgumentException("Serviço não pode ser nulo");
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
        for (Servico servico : servicos) {
            valorServicos += servico.getValor();
        }
        return valorServicos + getValorItens();
    }

    /**
     * Obtém o tipo da ordem (Entrada ou Saída).
     * @return Tipo da ordem
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Define o tipo da ordem.
     * @param tipo Tipo da ordem (Entrada ou Saída)
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtém a categoria da ordem.
     * @return Categoria da ordem
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Define a categoria da ordem.
     * @param categoria Categoria da ordem
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtém a descrição da ordem.
     * @return Descrição da ordem
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição da ordem.
     * @param descricao Descrição da ordem
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtém o valor total da ordem.
     * @return Valor total
     */
    public double getValor() {
        return valor;
    }

    /**
     * Define o valor total da ordem.
     * @param valor Valor total
     */
    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public void setResponsavel(Funcionario responsavel) {
        this.responsavel = responsavel;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Ordem de Serviço #").append(numeroOrdem != null ? numeroOrdem : "N/A").append(" ===\n");
        sb.append("Cliente: ").append(cliente != null ? cliente.getNome() : "N/A").append("\n");
        sb.append("Veículo: ").append(veiculo != null ? veiculo.getModelo() : "N/A").append("\n");
        sb.append("Data: ").append(data != null ? data : "N/A").append("\n");
        sb.append("Horário: ").append(horario != null ? horario : "N/A").append("\n");
        sb.append("Status: ").append(status != null ? status : "N/A").append("\n");
        sb.append("Responsável: ").append(responsavel != null ? responsavel.getNome() : "N/A").append("\n");
        
        // Lista de serviços
        if (servicos != null && !servicos.isEmpty()) {
            sb.append("Serviços:\n");
            for (Servico servico : servicos) {
                if (servico != null) {
                    sb.append("- ").append(servico.getDescricao())
                      .append(" (R$ ").append(String.format("%.2f", servico.getValor())).append(")\n");
                }
            }
        } else {
            sb.append("Serviços: Nenhum serviço registrado\n");
        }
        
        // Lista de peças utilizadas
        if (itensUtilizados != null && !itensUtilizados.isEmpty()) {
            sb.append("Peças Utilizadas:\n");
            for (Estoque.ItemEstoque item : itensUtilizados) {
                if (item != null) {
                    sb.append("- ").append(item.getNome())
                      .append(" x").append(item.getQuantidade())
                      .append(" (R$ ").append(String.format("%.2f", item.getPreco() * item.getQuantidade())).append(")\n");
                }
            }
        }
        
        sb.append("Valor Total: R$ ").append(String.format("%.2f", valor)).append("\n");
        
        if (elevadorAlocado != null) {
            sb.append("Elevador: ").append(elevadorAlocado).append("\n");
        }
        
        return sb.toString();
    }
}