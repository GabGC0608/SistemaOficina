package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Painel para gerenciamento de funcionários.
 */
public class FuncionarioPanel extends BasePanel {
    
    public FuncionarioPanel(Oficina oficina) {
        super(oficina, "Gerenciamento de Funcionários");
    }
    
    @Override
    protected void setupTable() {
        String[] colunas = {"Nome", "Telefone", "Endereço", "Especialidade", "Salário"};
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
        buttonPanel.add(createButton("Contratar Funcionário", e -> contratarFuncionario()));
        buttonPanel.add(createButton("Demitir Funcionário", e -> demitirFuncionario()));
        buttonPanel.add(createButton("Listar Funcionários", e -> listarFuncionarios()));
        buttonPanel.add(createButton("Buscar por Nome", e -> buscarFuncionario()));
    }
    
    @Override
    protected void setupForm() {
        // Formulário será implementado quando necessário
    }
    
    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        List<Funcionario> funcionarios = oficina.getFuncionarios();
        
        for (Funcionario funcionario : funcionarios) {
            Object[] row = {
                funcionario.getNome(),
                funcionario.getTelefone(),
                funcionario.getEndereco(),
                funcionario.getEspecialidades().isEmpty() ? "N/A" : 
                    String.join(", ", funcionario.getEspecialidades()),
                "R$ " + String.format("%.2f", funcionario.getSalario())
            };
            tableModel.addRow(row);
        }
    }
    
    @Override
    protected boolean saveFormData() {
        return true; // Implementar quando necessário
    }
    
    private void contratarFuncionario() {
        showMessage("Funcionalidade de contratação será implementada em breve!");
    }
    
    private void demitirFuncionario() {
        if (!hasSelection()) {
            showError("Selecione um funcionário para demitir!");
            return;
        }
        showMessage("Funcionalidade de demissão será implementada em breve!");
    }
    
    private void listarFuncionarios() {
        loadData();
        showMessage("Lista de funcionários atualizada!");
    }
    
    private void buscarFuncionario() {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do funcionário:");
        if (nome != null && !nome.trim().isEmpty()) {
            Funcionario funcionario = oficina.buscarFuncionarioPorNome(nome.trim());
            if (funcionario != null) {
                mostrarDetalhesFuncionario(funcionario);
            } else {
                showError("Funcionário não encontrado!");
            }
        }
    }
    
    private void mostrarDetalhesFuncionario(Funcionario funcionario) {
        StringBuilder detalhes = new StringBuilder();
        detalhes.append("Nome: ").append(funcionario.getNome()).append("\n");
        detalhes.append("Telefone: ").append(funcionario.getTelefone()).append("\n");
        detalhes.append("Endereço: ").append(funcionario.getEndereco()).append("\n");
        detalhes.append("Cargo: ").append(funcionario.getCargo()).append("\n");
        detalhes.append("Departamento: ").append(funcionario.getDepartamento()).append("\n");
        detalhes.append("Matrícula: ").append(funcionario.getMatricula()).append("\n");
        detalhes.append("Especialidades: ").append(
            funcionario.getEspecialidades().isEmpty() ? "Nenhuma" : 
            String.join(", ", funcionario.getEspecialidades())).append("\n");
        detalhes.append("Salário: R$ ").append(String.format("%.2f", funcionario.getSalario())).append("\n");
        
        JTextArea textArea = new JTextArea(detalhes.toString());
        textArea.setEditable(false);
        textArea.setFont(textArea.getFont().deriveFont(12f));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Detalhes do Funcionário", JOptionPane.INFORMATION_MESSAGE);
    }
} 