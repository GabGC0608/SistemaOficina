package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Painel para gerenciamento financeiro.
 */
public class FinanceiroPanel extends BasePanel {
    
    public FinanceiroPanel(Oficina oficina) {
        super(oficina, "Gerenciamento Financeiro");
    }
    
    @Override
    protected void setupTable() {
        String[] colunas = {"Data", "Tipo", "Descrição", "Valor", "Categoria", "Responsável"};
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
        buttonPanel.add(createButton("Registrar Entrada", e -> registrarEntrada()));
        buttonPanel.add(createButton("Registrar Saída", e -> registrarSaida()));
        buttonPanel.add(createButton("Gerar Balanço", e -> gerarBalanco()));
        buttonPanel.add(createButton("Listar Transações", e -> listarTransacoes()));
    }
    
    @Override
    protected void setupForm() {
        // Formulário será implementado quando necessário
    }
    
    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        for (OrdemServico os : oficina.getCaixa().getOrdensServico()) {
            Object[] row = {
                os.getData(),
                os.getTipo(),
                os.getDescricao(),
                String.format("R$ %.2f", os.getValor()),
                os.getCategoria(),
                os.getResponsavel() != null ? os.getResponsavel().getNome() : os.getResponsavel()
            };
            tableModel.addRow(row);
        }
    }
    
    @Override
    protected boolean saveFormData() {
        return true; // Implementar quando necessário
    }
    
    private void registrarEntrada() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField valorField = new JTextField();
        JTextField descricaoField = new JTextField();
        JTextField dataField = new JTextField();
        JTextField categoriaField = new JTextField();
        JTextField responsavelField = new JTextField();
        panel.add(new JLabel("Valor:")); panel.add(valorField);
        panel.add(new JLabel("Descrição:")); panel.add(descricaoField);
        panel.add(new JLabel("Data (dd/MM/yyyy):")); panel.add(dataField);
        panel.add(new JLabel("Categoria:")); panel.add(categoriaField);
        panel.add(new JLabel("Responsável:")); panel.add(responsavelField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Entrada", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double valor = Double.parseDouble(valorField.getText().trim());
                String descricao = descricaoField.getText().trim();
                String data = dataField.getText().trim();
                String categoria = categoriaField.getText().trim();
                String responsavel = responsavelField.getText().trim();
                OrdemServico os = new OrdemServico(
                    "Entrada", valor, descricao, data,
                    java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
                    categoria, responsavel, null, new java.util.ArrayList<>(), new java.util.ArrayList<>()
                );
                oficina.getCaixa().registrarEntrada(os);
                loadData();
                showMessage("Entrada registrada!");
            } catch (Exception ex) {
                showError("Dados inválidos: " + ex.getMessage());
            }
        }
    }
    
    private void registrarSaida() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField valorField = new JTextField();
        JTextField descricaoField = new JTextField();
        JTextField dataField = new JTextField();
        JTextField categoriaField = new JTextField();
        JTextField responsavelField = new JTextField();
        panel.add(new JLabel("Valor:")); panel.add(valorField);
        panel.add(new JLabel("Descrição:")); panel.add(descricaoField);
        panel.add(new JLabel("Data (dd/MM/yyyy):")); panel.add(dataField);
        panel.add(new JLabel("Categoria:")); panel.add(categoriaField);
        panel.add(new JLabel("Responsável:")); panel.add(responsavelField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Saída", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double valor = Double.parseDouble(valorField.getText().trim());
                String descricao = descricaoField.getText().trim();
                String data = dataField.getText().trim();
                String categoria = categoriaField.getText().trim();
                String responsavel = responsavelField.getText().trim();
                oficina.getCaixa().registrarSaida(valor, descricao, data, categoria, responsavel, new java.util.ArrayList<>());
                loadData();
                showMessage("Saída registrada!");
            } catch (Exception ex) {
                showError("Dados inválidos: " + ex.getMessage());
            }
        }
    }
    
    private void gerarBalanco() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField mesField = new JTextField();
        JTextField anoField = new JTextField();
        panel.add(new JLabel("Mês (1-12):")); panel.add(mesField);
        panel.add(new JLabel("Ano:")); panel.add(anoField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Gerar Balanço Mensal", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int mes = Integer.parseInt(mesField.getText().trim());
                int ano = Integer.parseInt(anoField.getText().trim());
                oficina.getCaixa().gerarBalancoMensal(mes, ano);
                showMessage("Balanço gerado no console.");
            } catch (Exception ex) {
                showError("Dados inválidos: " + ex.getMessage());
            }
        }
    }
    
    private void listarTransacoes() {
        loadData();
        showMessage("Lista de transações atualizada!");
    }
} 