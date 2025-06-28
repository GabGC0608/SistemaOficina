package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Painel para gerenciamento de clientes.
 */
public class ClientePanel extends BasePanel {
    
    private JTextField nomeField;
    private JTextField cpfField;
    private JTextField telefoneField;
    private JTextField enderecoField;
    
    public ClientePanel(Oficina oficina) {
        super(oficina, "Gerenciamento de Clientes");
    }
    
    @Override
    protected void setupTable() {
        String[] colunas = {"Nome", "Telefone", "Endereço", "CPF Anonimizado", "Qtd. Veículos"};
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
        buttonPanel.add(createButton("Adicionar Cliente", e -> adicionarCliente()));
        buttonPanel.add(createButton("Editar Cliente", e -> editarCliente()));
        buttonPanel.add(createButton("Remover Cliente", e -> removerCliente()));
        buttonPanel.add(createButton("Buscar por Nome", e -> buscarCliente()));
        buttonPanel.add(createButton("Adicionar Veículo", e -> adicionarVeiculo()));
    }
    
    @Override
    protected void setupForm() {
        nomeField = createTextField(20);
        telefoneField = createTextField(15);
        enderecoField = createTextField(30);
        cpfField = createTextField(15);
        
        int row = 0;
        addFormField(formPanel, "Nome", nomeField, row++);
        addFormField(formPanel, "Telefone", telefoneField, row++);
        addFormField(formPanel, "Endereço", enderecoField, row++);
        addFormField(formPanel, "CPF", cpfField, row++);
    }
    
    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        List<Cliente> clientes = oficina.getClientes();
        
        for (Cliente cliente : clientes) {
            Object[] row = {
                cliente.getNome(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.getCpfAnonimizado(),
                cliente.getVeiculos().size()
            };
            tableModel.addRow(row);
        }
    }
    
    @Override
    protected boolean saveFormData() {
        String nome = nomeField.getText().trim();
        String telefone = telefoneField.getText().trim();
        String endereco = enderecoField.getText().trim();
        String cpf = cpfField.getText().trim();
        
        if (nome.isEmpty() || telefone.isEmpty() || endereco.isEmpty() || cpf.isEmpty()) {
            showError("Todos os campos são obrigatórios!");
            return false;
        }
        
        try {
            // Usar o método da oficina para cadastrar cliente
            // Como o método original usa Scanner, vamos simular a criação
            String cpfAnonimizado = oficina.anonimizarCPF(cpf);
            Cliente cliente = new Cliente(nome, telefone, endereco, cpfAnonimizado);
            oficina.getClientes().add(cliente);
            showMessage("Cliente adicionado com sucesso!");
            clearForm();
            return true;
        } catch (Exception e) {
            showError("Erro ao adicionar cliente: " + e.getMessage());
            return false;
        }
    }
    
    private void adicionarCliente() {
        clearForm();
        showFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Adicionar Cliente");
    }
    
    private void editarCliente() {
        if (!hasSelection()) {
            showError("Selecione um cliente para editar!");
            return;
        }
        
        int row = getSelectedRowIndex();
        String telefone = (String) table.getValueAt(row, 1);
        Cliente cliente = oficina.getClientes().stream()
            .filter(c -> c.getTelefone().equals(telefone))
            .findFirst()
            .orElse(null);
        
        if (cliente != null) {
            preencherFormulario(cliente);
            showFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Editar Cliente");
        }
    }
    
    private void removerCliente() {
        if (!hasSelection()) {
            showError("Selecione um cliente para remover!");
            return;
        }
        
        int row = getSelectedRowIndex();
        String telefone = (String) table.getValueAt(row, 1);
        
        if (confirmAction("Deseja realmente remover o cliente com telefone " + telefone + "?")) {
            try {
                boolean removido = oficina.getClientes().removeIf(c -> c.getTelefone().equals(telefone));
                if (removido) {
                    showMessage("Cliente removido com sucesso!");
                    loadData();
                } else {
                    showError("Cliente não encontrado!");
                }
            } catch (Exception e) {
                showError("Erro ao remover cliente: " + e.getMessage());
            }
        }
    }
    
    private void buscarCliente() {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do cliente:");
        if (nome != null && !nome.trim().isEmpty()) {
            Cliente cliente = oficina.getClientes().stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nome.trim()))
                .findFirst()
                .orElse(null);
            
            if (cliente != null) {
                mostrarDetalhesCliente(cliente);
            } else {
                showError("Cliente não encontrado!");
            }
        }
    }
    
    private void adicionarVeiculo() {
        if (!hasSelection()) {
            showError("Selecione um cliente para adicionar veículo!");
            return;
        }
        
        int row = getSelectedRowIndex();
        String telefoneCliente = (String) table.getValueAt(row, 1);
        Cliente cliente = oficina.getClientes().stream()
            .filter(c -> c.getTelefone().equals(telefoneCliente))
            .findFirst()
            .orElse(null);
        
        if (cliente != null) {
            VeiculoDialog veiculoDialog = new VeiculoDialog((JFrame) SwingUtilities.getWindowAncestor(this), oficina, cliente);
            veiculoDialog.setVisible(true);
            if (veiculoDialog.isVeiculoAdicionado()) {
                loadData();
            }
        }
    }
    
    private void preencherFormulario(Cliente cliente) {
        nomeField.setText(cliente.getNome());
        telefoneField.setText(cliente.getTelefone());
        enderecoField.setText(cliente.getEndereco());
        cpfField.setText(cliente.getCpfAnonimizado());
    }
    
    private void mostrarDetalhesCliente(Cliente cliente) {
        StringBuilder detalhes = new StringBuilder();
        detalhes.append("Nome: ").append(cliente.getNome()).append("\n");
        detalhes.append("Telefone: ").append(cliente.getTelefone()).append("\n");
        detalhes.append("Endereço: ").append(cliente.getEndereco()).append("\n");
        detalhes.append("CPF Anonimizado: ").append(cliente.getCpfAnonimizado()).append("\n");
        detalhes.append("Quantidade de Veículos: ").append(cliente.getVeiculos().size()).append("\n");
        
        if (!cliente.getVeiculos().isEmpty()) {
            detalhes.append("\nVeículos:\n");
            for (Veiculo veiculo : cliente.getVeiculos()) {
                detalhes.append("- ").append(veiculo.getMarca()).append(" ").append(veiculo.getModelo())
                       .append(" (").append(veiculo.getPlaca()).append(")\n");
            }
        }
        
        JTextArea textArea = new JTextArea(detalhes.toString());
        textArea.setEditable(false);
        textArea.setFont(textArea.getFont().deriveFont(12f));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Detalhes do Cliente", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    protected void clearForm() {
        nomeField.setText("");
        telefoneField.setText("");
        enderecoField.setText("");
        cpfField.setText("");
    }
} 