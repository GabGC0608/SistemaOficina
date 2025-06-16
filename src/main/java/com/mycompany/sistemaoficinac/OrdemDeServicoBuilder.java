package com.mycompany.sistemaoficinac;

public interface OrdemDeServicoBuilder {
    void reset();
    void setTipo(String tipo);
    void setDescricao(String descricao);
    void setData(String data);
    void setHorario(String hora);
    void setCategoria(String categoria);
    void setResponsavel(String responsavel);
    void setCliente(String cliente);
    void setStatus(String  status);
    
    void adicionarItem(Estoque.ItemEstoque item, int quantidade);
    void adicionarServico(Servico servico);
        
       
    
    OrdemServico build();
}