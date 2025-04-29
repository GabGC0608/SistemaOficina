package com.mycompany.sistemaoficinac;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que gerencia o estoque da oficina.
 */
public class Estoque {
    private List<ItemEstoque> itens; // Lista de itens no estoque

    /**
     * Construtor da classe Estoque.
     * Inicializa a lista de itens como vazia.
     */
    public Estoque() {
        this.itens = new ArrayList<>();
    }

    /**
     * Classe interna que representa um item no estoque.
     */
    public static class ItemEstoque {
        private String codigo; // Código do item
        private String nome; // Nome do item
        private int quantidade; // Quantidade disponível no estoque

        /**
         * Construtor da classe ItemEstoque.
         *
         * @param codigo Código do item.
         * @param nome Nome do item.
         * @param quantidade Quantidade do item.
         */
        public ItemEstoque(@JsonProperty("codigo") String codigo,
                           @JsonProperty("nome") String nome,
                           @JsonProperty("quantidade") int quantidade) {
            this.codigo = codigo;
            this.nome = nome;
            this.quantidade = quantidade;
        }

        /**
         * Obtém o código do item.
         *
         * @return Código do item.
         */
        public String getCodigo() {
            return codigo;
        }

        /**
         * Obtém o nome do item.
         *
         * @return Nome do item.
         */
        public String getNome() {
            return nome;
        }

        /**
         * Obtém a quantidade do item.
         *
         * @return Quantidade do item.
         */
        public int getQuantidade() {
            return quantidade;
        }

        /**
         * Define a quantidade do item.
         *
         * @param quantidade Nova quantidade do item.
         */
        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        /**
         * Retorna uma representação textual do item.
         *
         * @return String representando o item.
         */
        @Override
        public String toString() {
            return String.format("%s - %s | Quantidade: %d", codigo, nome, quantidade);
        }
    }

    /**
     * Adiciona um novo item ao estoque.
     *
     * @param codigo Código do item.
     * @param nome Nome do item.
     * @param quantidade Quantidade do item.
     */
    public void adicionarItem(String codigo, String nome, int quantidade) {
        itens.add(new ItemEstoque(codigo, nome, quantidade));
        System.out.println("Item adicionado: " + nome);
    }

    /**
     * Remove um item do estoque com base no código.
     *
     * @param codigo Código do item a ser removido.
     */
    public void removerItem(String codigo) {
        itens.removeIf(item -> item.getCodigo().equals(codigo));
        System.out.println("Item removido com sucesso!");
    }

    /**
     * Atualiza a quantidade de um item no estoque.
     *
     * @param codigo Código do item.
     * @param novaQuantidade Nova quantidade do item.
     */
    public void atualizarQuantidade(String codigo, int novaQuantidade) {
        itens.stream()
            .filter(item -> item.getCodigo().equals(codigo))
            .findFirst()
            .ifPresent(item -> item.setQuantidade(novaQuantidade));
        System.out.println("Quantidade atualizada com sucesso!");
    }

    /**
     * Lista todos os itens do estoque.
     */
    public void listarItens() {
        System.out.println("\n=== ITENS NO ESTOQUE ===");
        itens.forEach(System.out::println);
    }

    /**
     * Obtém a lista de itens do estoque.
     *
     * @return Lista de itens.
     */
    public List<ItemEstoque> getItens() {
        return itens;
    }
}