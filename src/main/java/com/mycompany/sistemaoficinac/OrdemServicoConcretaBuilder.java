package com.mycompany.sistemaoficinac;

public class OrdemServicoConcretaBuilder implements OrdemDeServicoBuilder {
    private OrdemServico ordem;

    public OrdemServicoConcretaBuilder() {
        this.reset();
    }

    @Override
    public void reset() {
        this.ordem = new OrdemServico();
    }

    @Override
    public void setTipo(String tipo) {
        ordem.setTipo(tipo);
    }

    @Override
    public void setDescricao(String descricao) {
        ordem.setDescricao(descricao);
    }

    @Override
    public void setData(String data) {
        ordem.setData(data);
    }

    @Override
    public void setHorario(String hora) {
        ordem.setHorario(hora);
    }

    @Override
    public void setCategoria(String categoria) {
        ordem.setCategoria(categoria);
    }

    @Override
    public void setResponsavel(String responsavel) {
        // Create a temporary Funcionario object with the name
        Funcionario func = new Funcionario(responsavel, "", "", "Mecânico", 0.0, "TEMP", "Temporário");
        ordem.setResponsavel(func);
    }

    @Override
    public void setCliente(String cliente) {
        // Create a temporary Cliente object with the name
        Cliente cli = new Cliente(cliente, "", "", "");
        ordem.setCliente(cli);
    }

    @Override
    public void setStatus(String status) {
        ordem.setStatus(status);
    }

    @Override
    public void adicionarItem(Estoque.ItemEstoque item, int quantidade) {
        ordem.adicionarItemEstoque(item, quantidade);
    }

    @Override
    public void adicionarServico(Servico servico) {
        ordem.adicionarServico(servico);
    }

    @Override
    public OrdemServico build() {
        return ordem;
    }
}