package com.mycompany.sistemaoficinac;

import java.util.List;

public class OrdemServicoDirector {
    private OrdemDeServicoBuilder builder;

    public OrdemServicoDirector(OrdemDeServicoBuilder builder) {
        this.builder = builder;
    }

    public void construirOrdemComPecas(String tipo, String descricao, String data, String categoria,
                                       String responsavel, String cliente,
                                       List<Estoque.ItemEstoque> pecas) {
        builder.reset();
        builder.setTipo(tipo);
        builder.setDescricao(descricao);
        builder.setData(data);
        builder.setCategoria(categoria);
        builder.setResponsavel(responsavel);
        builder.setCliente(cliente);
        for (Estoque.ItemEstoque item : pecas) {
            builder.adicionarItem(item, item.getQuantidade());
        }
    }

    public void construirOrdemComServicos(String tipo, String descricao, String data, String categoria,
                                          String responsavel, String cliente,
                                          Agenda.Agendamento agendamento) {
        builder.reset();
        builder.setTipo(tipo);
        builder.setDescricao(descricao);
        builder.setData(data);
        builder.setCategoria(categoria);
        builder.setResponsavel(responsavel);
        builder.setCliente(cliente);
        builder.setAgendamento(agendamento);
    }

    public void construirOrdemCompleta(String tipo, String descricao, String data, String categoria,
                                       String responsavel, String cliente,
                                       Agenda.Agendamento agendamento,
                                       List<Estoque.ItemEstoque> pecas) {
        construirOrdemComServicos(tipo, descricao, data, categoria, responsavel, cliente, agendamento);
        for (Estoque.ItemEstoque item : pecas) {
            builder.adicionarItem(item, item.getQuantidade());
        }
    }
}
