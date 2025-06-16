package com.mycompany.sistemaoficinac;

import java.util.List;

public class OrdemServicoDirector {
    private OrdemDeServicoBuilder builder;

    public OrdemServicoDirector(OrdemDeServicoBuilder builder) {
        this.builder = builder;
    }

    public void construirOrdemComPecas(String tipo, String data,String horario, String categoria,
                                       String responsavel, String cliente,
                                       List<Estoque.ItemEstoque> pecas) {
        builder.reset();
        builder.setTipo(tipo);
        builder.setDescricao("Apenas peças utilizadas");
        builder.setData(data);
        builder.setHorario(horario);
        builder.adicionarItem(null, 0);
        builder.setCategoria(categoria);
        builder.setResponsavel(responsavel);
        builder.setCliente(cliente);
        for (Estoque.ItemEstoque item : pecas) {
            builder.adicionarItem(item, item.getQuantidade());
        }
    }

    public void construirOrdemComServicos(String tipo, String data,String horario, String categoria,
                                          String responsavel, String cliente,
                                          String status,
                                          List<Servico> servicos) {
        builder.reset();
        builder.setTipo(tipo);
        builder.setDescricao("penas servicos");
        builder.setData(data);
        builder.setCategoria(categoria);
        builder.setResponsavel(responsavel);
        builder.setCliente(cliente);
        builder.setStatus(status);
        for (Servico servico : servicos)
            builder.adicionarServico(servico);
    }

    public void construirOrdemCompleta(String tipo , String data,String horario, String categoria,
                                       String responsavel, String cliente,
                                       String status,
                                       List<Estoque.ItemEstoque> pecas,
                                       List<Servico> servicos) {
       builder.reset();
        builder.setTipo(tipo);
        builder.setDescricao("Servicos e peças");
        builder.setData(data);
        builder.setHorario(horario);
        builder.setStatus(status);
        builder.setCategoria(categoria);
        builder.setResponsavel(responsavel);
        builder.setCliente(cliente);
        for (Estoque.ItemEstoque item : pecas) {
            builder.adicionarItem(item, item.getQuantidade());
        }
        for(Servico servico : servicos){
            builder.adicionarServico(servico);
        }

    }
}