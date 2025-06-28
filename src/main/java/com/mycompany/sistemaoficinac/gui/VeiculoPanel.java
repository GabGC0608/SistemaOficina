package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Painel para gerenciamento de veículos.
 */
public class VeiculoPanel extends BasePanel {
    
    public VeiculoPanel(Oficina oficina) {
        super(oficina, "Gerenciamento de Veículos");
    }
    
    @Override
    protected void setupTable() {
        String[] colunas = {"Marca", "Modelo", "Placa", "Ano", "Cor", "Cliente"};
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
        buttonPanel.add(createButton("Listar Todos", e -> listarTodosVeiculos()));
        buttonPanel.add(createButton("Buscar por Placa", e -> buscarVeiculo()));
        buttonPanel.add(createButton("Mostrar Total", e -> mostrarTotal()));
    }
    
    @Override
    protected void setupForm() {
        // Formulário não é usado neste painel
    }
    
    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        List<Veiculo> todosVeiculos = new ArrayList<>();
        
        // Coletar todos os veículos de todos os clientes
        for (Cliente cliente : oficina.getClientes()) {
            for (Veiculo veiculo : cliente.getVeiculos()) {
                todosVeiculos.add(veiculo);
            }
        }
        
        for (Veiculo veiculo : todosVeiculos) {
            // Encontrar o cliente proprietário
            String nomeCliente = "Não encontrado";
            for (Cliente cliente : oficina.getClientes()) {
                if (cliente.getVeiculos().contains(veiculo)) {
                    nomeCliente = cliente.getNome();
                    break;
                }
            }
            
            Object[] row = {
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getPlaca(),
                veiculo.getAno(),
                veiculo.getCor(),
                nomeCliente
            };
            tableModel.addRow(row);
        }
    }
    
    @Override
    protected boolean saveFormData() {
        return true; // Não usado neste painel
    }
    
    private void listarTodosVeiculos() {
        loadData();
        showMessage("Lista de veículos atualizada!");
    }
    
    private void buscarVeiculo() {
        String placa = JOptionPane.showInputDialog(this, "Digite a placa do veículo:");
        if (placa != null && !placa.trim().isEmpty()) {
            Veiculo veiculo = oficina.buscarVeiculoPorPlaca(placa.trim());
            if (veiculo != null) {
                mostrarDetalhesVeiculo(veiculo);
            } else {
                showError("Veículo não encontrado!");
            }
        }
    }
    
    private void mostrarTotal() {
        int total = 0;
        for (Cliente cliente : oficina.getClientes()) {
            total += cliente.getVeiculos().size();
        }
        
        showMessage("Total de veículos cadastrados: " + total);
    }
    
    private void mostrarDetalhesVeiculo(Veiculo veiculo) {
        // Encontrar o cliente proprietário
        Cliente proprietario = null;
        for (Cliente cliente : oficina.getClientes()) {
            if (cliente.getVeiculos().contains(veiculo)) {
                proprietario = cliente;
                break;
            }
        }
        
        StringBuilder detalhes = new StringBuilder();
        detalhes.append("Marca: ").append(veiculo.getMarca()).append("\n");
        detalhes.append("Modelo: ").append(veiculo.getModelo()).append("\n");
        detalhes.append("Placa: ").append(veiculo.getPlaca()).append("\n");
        detalhes.append("Ano: ").append(veiculo.getAno()).append("\n");
        detalhes.append("Cor: ").append(veiculo.getCor()).append("\n");
        
        if (proprietario != null) {
            detalhes.append("Proprietário: ").append(proprietario.getNome()).append("\n");
            detalhes.append("Telefone: ").append(proprietario.getTelefone()).append("\n");
        } else {
            detalhes.append("Proprietário: Não encontrado\n");
        }
        
        JTextArea textArea = new JTextArea(detalhes.toString());
        textArea.setEditable(false);
        textArea.setFont(textArea.getFont().deriveFont(12f));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Detalhes do Veículo", JOptionPane.INFORMATION_MESSAGE);
    }
} 