package com.mycompany.sistemaoficina;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Estoque {
    private List<ItemEstoque> itens;

    public Estoque() {
        this.itens = new ArrayList<>();
    }

    public static class ItemEstoque {
        private String codigo;
        private String nome;
        private int quantidade;

        public ItemEstoque(@JsonProperty("codigo")String codigo,@JsonProperty("nome") String nome, @JsonProperty("quantidade")int quantidade) {
            this.codigo = codigo;
            this.nome = nome;
            this.quantidade = quantidade;
        }

        // Getters e Setters
        public String getCodigo() { return codigo; }
        public String getNome() { return nome; }
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

        @Override
        public String toString() {
            return String.format("%s - %s | Quantidade: %d", codigo, nome, quantidade);
        }
    }

    // Métodos do estoque
    public void adicionarItem(String codigo, String nome, int quantidade) {
        itens.add(new ItemEstoque(codigo, nome, quantidade));
        System.out.println("Item adicionado: " + nome);
    }

    public void removerItem(String codigo) {
        itens.removeIf(item -> item.getCodigo().equals(codigo));
    }

    public void atualizarQuantidade(String codigo, int novaQuantidade) {
        itens.stream()
            .filter(item -> item.getCodigo().equals(codigo))
            .findFirst()
            .ifPresent(item -> item.setQuantidade(novaQuantidade));
    }

    public void listarItens() {
        itens.forEach(System.out::println);
    }

    public List<ItemEstoque> getItens() {
        return itens;
    }
}