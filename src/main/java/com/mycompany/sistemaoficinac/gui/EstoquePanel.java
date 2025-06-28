package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Painel para gerenciamento de estoque.
 */
public class EstoquePanel extends BasePanel {
    
    public EstoquePanel(Oficina oficina) {
        super(oficina, "Gerenciamento de Estoque");
    }
    
    @Override
    protected void setupTable() {
        String[] colunas = {"Código", "Nome", "Quantidade", "Preço Unitário"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
    }
    
    @Override
    protected void setupButtons() {
        buttonPanel.add(createButton("Adicionar Item", e -> adicionarItem()));
        buttonPanel.add(createButton("Remover Item", e -> removerItem()));
        buttonPanel.add(createButton("Atualizar Item", e -> atualizarItem()));
        buttonPanel.add(createButton("Vender Peças", e -> venderPecas()));
        buttonPanel.add(createButton("Listar Estoque", e -> listarEstoque()));
    }
    
    @Override
    protected void setupForm() {
        // Formulário será implementado quando necessário
    }
    
    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        for (Estoque.ItemEstoque item : oficina.getEstoque().getItens()) {
            Object[] row = {
                item.getCodigo(),
                item.getNome(),
                item.getQuantidade(),
                String.format("R$ %.2f", item.getPreco())
            };
            tableModel.addRow(row);
        }
    }
    
    @Override
    protected boolean saveFormData() {
        return true; // Implementar quando necessário
    }
    
    private void adicionarItem() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField codigoField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField quantidadeField = new JTextField();
        JTextField precoField = new JTextField();
        JTextField descricaoField = new JTextField();
        panel.add(new JLabel("Código:")); panel.add(codigoField);
        panel.add(new JLabel("Nome:")); panel.add(nomeField);
        panel.add(new JLabel("Quantidade:")); panel.add(quantidadeField);
        panel.add(new JLabel("Preço Unitário:")); panel.add(precoField);
        panel.add(new JLabel("Descrição:")); panel.add(descricaoField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Item ao Estoque", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String codigo = codigoField.getText().trim();
                String nome = nomeField.getText().trim();
                int quantidade = Integer.parseInt(quantidadeField.getText().trim());
                double preco = Double.parseDouble(precoField.getText().trim());
                String descricao = descricaoField.getText().trim();
                oficina.getEstoque().adicionarItem(codigo, nome, quantidade, preco, descricao);
                loadData();
                showMessage("Item adicionado com sucesso!");
            } catch (Exception ex) {
                showError("Dados inválidos: " + ex.getMessage());
            }
        }
    }
    
    private void removerItem() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Selecione um item para remover!");
            return;
        }
        String codigo = (String) tableModel.getValueAt(row, 0);
        if (confirmAction("Remover o item de código '" + codigo + "'?")) {
            oficina.getEstoque().removerItem(codigo);
            loadData();
            showMessage("Item removido!");
        }
    }
    
    private void atualizarItem() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Selecione um item para atualizar!");
            return;
        }
        String codigo = (String) tableModel.getValueAt(row, 0);
        Estoque.ItemEstoque item = oficina.getEstoque().buscarItemPorCodigo(codigo);
        if (item == null) {
            showError("Item não encontrado!");
            return;
        }
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField nomeField = new JTextField(item.getNome());
        JTextField quantidadeField = new JTextField(String.valueOf(item.getQuantidade()));
        JTextField precoField = new JTextField(String.valueOf(item.getPreco()));
        JTextField descricaoField = new JTextField(item.getDescricao());
        panel.add(new JLabel("Nome:")); panel.add(nomeField);
        panel.add(new JLabel("Quantidade:")); panel.add(quantidadeField);
        panel.add(new JLabel("Preço Unitário:")); panel.add(precoField);
        panel.add(new JLabel("Descrição:")); panel.add(descricaoField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Atualizar Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                item.setNome(nomeField.getText().trim());
                item.setQuantidade(Integer.parseInt(quantidadeField.getText().trim()));
                item.setPreco(Double.parseDouble(precoField.getText().trim()));
                item.setDescricao(descricaoField.getText().trim());
                loadData();
                showMessage("Item atualizado!");
            } catch (Exception ex) {
                showError("Dados inválidos: " + ex.getMessage());
            }
        }
    }
    
    private void venderPecas() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Selecione um item para vender!");
            return;
        }
        String codigo = (String) tableModel.getValueAt(row, 0);
        Estoque.ItemEstoque item = oficina.getEstoque().buscarItemPorCodigo(codigo);
        if (item == null) {
            showError("Item não encontrado!");
            return;
        }
        String quantidadeStr = JOptionPane.showInputDialog(this, "Quantidade a vender:", "Venda de Peças", JOptionPane.QUESTION_MESSAGE);
        if (quantidadeStr == null) return;
        try {
            int quantidade = Integer.parseInt(quantidadeStr.trim());
            if (quantidade <= 0 || quantidade > item.getQuantidade()) {
                showError("Quantidade inválida ou insuficiente em estoque!");
                return;
            }
            oficina.getEstoque().venderItem(codigo, quantidade);
            loadData();
            showMessage("Venda realizada!");
        } catch (Exception ex) {
            showError("Valor inválido: " + ex.getMessage());
        }
    }
    
    private void listarEstoque() {
        loadData();
        showMessage("Lista de estoque atualizada!");
    }
} 