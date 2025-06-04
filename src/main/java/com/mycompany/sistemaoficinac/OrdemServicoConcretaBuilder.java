package com.mycompany.sistemaoficinac;

import java.util.ArrayList;

public class OrdemServicoConcretaBuilder implements OrdemDeServicoBuilder {
    private OrdemServico ordem;

    public OrdemServicoConcretaBuilder() {
        this.reset();
    }

    @Override
    public void reset() {
        ordem = new OrdemServico();
        ordem.setItensUtilizados(new ArrayList<>());
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
    public void setCategoria(String categoria) {
        ordem.setCategoria(categoria);
    }

    @Override
    public void setResponsavel(String responsavel) {
        ordem.setResponsavel(responsavel);
    }

    @Override
    public void setCliente(String cliente) {
        ordem.setCliente(cliente);
    }

    @Override
    public void setAgendamento(Agenda.Agendamento agendamento) {
        ordem.setAgendamento(agendamento);
    }

    @Override
    public void adicionarItem(Estoque.ItemEstoque item, int quantidade) {
        ordem.adicionarItemEstoque(item, quantidade);
    }

    @Override
    public OrdemServico build() {
        ordem.setValor(ordem.getValor()); // força recálculo
        OrdemServico resultado = ordem;
        this.reset();
        return resultado;
    }
}
