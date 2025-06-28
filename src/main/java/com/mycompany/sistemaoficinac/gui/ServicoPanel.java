package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Painel para gerenciamento de serviços.
 */
public class ServicoPanel extends BasePanel {
    
    private JTextField nomeField;
    private JTextField descricaoField;
    private JTextField precoField;
    private JTextField tempoEstimadoField;
    
    public ServicoPanel(Oficina oficina) {
        super(oficina, "Gerenciamento de Serviços");
    }
    
    @Override
    protected void setupTable() {
        String[] colunas = {"Nome", "Descrição", "Preço", "Tempo Estimado"};
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
        buttonPanel.add(createButton("Adicionar Serviço", e -> adicionarServico()));
        buttonPanel.add(createButton("Remover Serviço", e -> removerServico()));
        buttonPanel.add(createButton("Listar Serviços", e -> listarServicos()));
    }
    
    @Override
    protected void setupForm() {
        nomeField = createTextField(20);
        descricaoField = createTextField(30);
        precoField = createTextField(10);
        tempoEstimadoField = createTextField(10);
        
        int row = 0;
        addFormField(formPanel, "Nome", nomeField, row++);
        addFormField(formPanel, "Descrição", descricaoField, row++);
        addFormField(formPanel, "Preço", precoField, row++);
        addFormField(formPanel, "Tempo Estimado (min)", tempoEstimadoField, row++);
    }
    
    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        List<Servico> servicos = oficina.getServicos();
        
        for (Servico servico : servicos) {
            Object[] row = {
                servico.getNome(),
                servico.getDescricao(),
                "R$ " + String.format("%.2f", servico.getValor()),
                servico.getTempoEstimado() + " min"
            };
            tableModel.addRow(row);
        }
    }
    
    @Override
    protected boolean saveFormData() {
        String nome = nomeField.getText().trim();
        String descricao = descricaoField.getText().trim();
        String precoStr = precoField.getText().trim();
        String tempoStr = tempoEstimadoField.getText().trim();
        
        if (nome.isEmpty() || descricao.isEmpty() || precoStr.isEmpty() || tempoStr.isEmpty()) {
            showError("Todos os campos são obrigatórios!");
            return false;
        }
        
        try {
            double preco = Double.parseDouble(precoStr);
            int tempo = Integer.parseInt(tempoStr);
            
            Servico servico = new Servico(nome, descricao, preco, tempo);
            oficina.getServicos().add(servico);
            showMessage("Serviço adicionado com sucesso!");
            clearForm();
            return true;
        } catch (NumberFormatException e) {
            showError("Preço e tempo devem ser números válidos!");
            return false;
        } catch (Exception e) {
            showError("Erro ao adicionar serviço: " + e.getMessage());
            return false;
        }
    }
    
    private void adicionarServico() {
        clearForm();
        showFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Adicionar Serviço");
    }
    
    private void removerServico() {
        if (!hasSelection()) {
            showError("Selecione um serviço para remover!");
            return;
        }
        
        int row = getSelectedRowIndex();
        String nome = (String) table.getValueAt(row, 0);
        
        if (confirmAction("Deseja realmente remover o serviço " + nome + "?")) {
            try {
                boolean removido = oficina.getServicos().removeIf(s -> s.getNome().equals(nome));
                if (removido) {
                    showMessage("Serviço removido com sucesso!");
                    loadData();
                } else {
                    showError("Serviço não encontrado!");
                }
            } catch (Exception e) {
                showError("Erro ao remover serviço: " + e.getMessage());
            }
        }
    }
    
    private void listarServicos() {
        loadData();
        showMessage("Lista de serviços atualizada!");
    }
    
    @Override
    protected void clearForm() {
        nomeField.setText("");
        descricaoField.setText("");
        precoField.setText("");
        tempoEstimadoField.setText("");
    }
} 