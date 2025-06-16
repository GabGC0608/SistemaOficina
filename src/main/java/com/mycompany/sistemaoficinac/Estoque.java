package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que gerencia o estoque da oficina.
 */
public class Estoque {
    @JsonProperty("itens")
    private List<ItemEstoque> itens; // Lista de itens no estoque

    /**
     * Construtor da classe Estoque.
     * Inicializa a lista de itens como vazia.
     */
    @JsonCreator
    public Estoque(@JsonProperty("itens") List<ItemEstoque> itens) {
        this.itens = itens != null ? itens : new ArrayList<>();
    }

    /**
     * Construtor padrão.
     */
    public Estoque() {
        this.itens = new ArrayList<>();
    }

/**
 * Classe interna que representa um item no estoque.
 */
public static class ItemEstoque {
    @JsonProperty("codigo")
    private String codigo; // Código do item
    @JsonProperty("nome")
    private String nome; // Nome do item
    @JsonProperty("quantidade")
    private int quantidade; // Quantidade disponível no estoque
    @JsonProperty("preco")
    private double preco; // Preço unitário do item
    @JsonProperty("descricao")
    private String descricao; // Descrição do item

    /**
     * Construtor da classe ItemEstoque.
     *
     * @param codigo Código do item.
     * @param nome Nome do item.
     * @param quantidade Quantidade do item.
     * @param preco Preço unitário do item.
     * @param descricao Descrição do item.
     */
    @JsonCreator
    public ItemEstoque(@JsonProperty("codigo") String codigo,
                      @JsonProperty("nome") String nome,
                      @JsonProperty("quantidade") int quantidade,
                      @JsonProperty("preco") double preco,
                      @JsonProperty("descricao") String descricao) {
        this.codigo = codigo;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.descricao = descricao;
    }

    /**
     * Construtor simplificado para itens sem preço e descrição.
     */
    public ItemEstoque(String codigo, String nome, int quantidade) {
        this(codigo, nome, quantidade, 0.0, "");
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
     * Obtém o preço unitário do item.
     *
     * @return Preço unitário do item.
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Define o preço unitário do item.
     *
     * @param preco Novo preço unitário do item.
     */
    public void setPreco(double preco) {
        this.preco = preco;
    }

    /**
     * Obtém a descrição do item.
     *
     * @return Descrição do item.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição do item.
     *
     * @param descricao Nova descrição do item.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna uma representação textual do item.
     *
     * @return String representando o item.
     */
    @Override
    public String toString() {
        return String.format("%s - %s | Quantidade: %d | Preço: R$ %.2f | %s", 
                           codigo, nome, quantidade, preco, descricao);
    }
}

    /**
     * Adiciona um novo item ao estoque.
     *
     * @param codigo Código do item.
     * @param nome Nome do item.
     * @param quantidade Quantidade do item.
     * @param preco Preço unitário do item.
     * @param descricao Descrição do item.
     */
    public void adicionarItem(String codigo, String nome, int quantidade, double preco, String descricao) {
        itens.add(new ItemEstoque(codigo, nome, quantidade, preco, descricao));
        System.out.println("Item adicionado: " + nome);
    }

    /**
     * Adiciona um novo item ao estoque.
     *
     * @param codigo Código do item.
     * @param nome Nome do item.
     * @param quantidade Quantidade do item.
     */
    public void adicionarItem(String codigo, String nome, int quantidade) {
        adicionarItem(codigo, nome, quantidade, 0.0, "");
    }

    /**
     * Adiciona um item já existente ao estoque.
     *
     * @param item Item a ser adicionado.
     */
    public void adicionarItem(ItemEstoque item) {
        itens.add(item);
        System.out.println("Item adicionado: " + item.getNome());
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
    public void venderItem(String codigo, int quantidade) {
        for (ItemEstoque item : itens) {
            if (item.getCodigo().equals(codigo)) {
                if (item.getQuantidade() >= quantidade) {
                    item.setQuantidade(item.getQuantidade() - quantidade);
                    System.out.println("Venda realizada com sucesso!");
                } else {
                    System.out.println("Quantidade insuficiente em estoque.");
                }
                return;
            }
        }
        System.out.println("Item não encontrado no estoque.");
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

    /**
     * Busca um item no estoque pelo código.
     *
     * @param codigo Código do item a ser buscado.
     * @return O item do estoque se encontrado, null caso contrário.
     */
    public ItemEstoque buscarItemPorCodigo(String codigo) {
        for (ItemEstoque item : itens) {
            if (item.getCodigo().equals(codigo)) {
                return item;
            }
        }
        return null;
    }
}