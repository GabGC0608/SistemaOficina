package com.mycompany.sistemaoficinac;

public interface OrdemDeServicoBuilder {
    void reset();
    void setTipo(String tipo);
    void setDescricao(String descricao);
    void setData(String data);
    void setCategoria(String categoria);
    void setResponsavel(String responsavel);
    void setCliente(String cliente);
    void setAgendamento(Agenda.Agendamento agendamento);
    void adicionarItem(Estoque.ItemEstoque item, int quantidade);
    OrdemServico build();
}
