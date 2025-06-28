package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.*;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo para adicionar veículos aos clientes.
 */
public class VeiculoDialog extends JDialog {
    
    private final Oficina oficina;
    private final Cliente cliente;
    private boolean veiculoAdicionado = false;
    
    private JTextField marcaField;
    private JTextField modeloField;
    private JTextField placaField;
    private JTextField anoField;
    private JTextField corField;
    
    public VeiculoDialog(JFrame parent, Oficina oficina, Cliente cliente) {
        super(parent, "Adicionar Veículo", true);
        this.oficina = oficina;
        this.cliente = cliente;
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Título
        JLabel titleLabel = new JLabel("Adicionar Veículo para " + cliente.getNome(), SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(titleLabel, BorderLayout.NORTH);
        
        // Painel do formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campos do formulário
        marcaField = new JTextField(20);
        modeloField = new JTextField(20);
        placaField = new JTextField(10);
        anoField = new JTextField(4);
        corField = new JTextField(15);
        
        int row = 0;
        
        // Marca
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(marcaField, gbc);
        
        row++;
        
        // Modelo
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(modeloField, gbc);
        
        row++;
        
        // Placa
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Placa:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(placaField, gbc);
        
        row++;
        
        // Ano
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(anoField, gbc);
        
        row++;
        
        // Cor
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Cor:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(corField, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        JButton salvarButton = new JButton("Salvar");
        JButton cancelarButton = new JButton("Cancelar");
        
        salvarButton.addActionListener(e -> salvarVeiculo());
        cancelarButton.addActionListener(e -> dispose());
        
        buttonPanel.add(salvarButton);
        buttonPanel.add(cancelarButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupLayout() {
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    private void salvarVeiculo() {
        String marca = marcaField.getText().trim();
        String modelo = modeloField.getText().trim();
        String placa = placaField.getText().trim();
        String anoStr = anoField.getText().trim();
        String cor = corField.getText().trim();
        
        if (marca.isEmpty() || modelo.isEmpty() || placa.isEmpty() || anoStr.isEmpty() || cor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int ano = Integer.parseInt(anoStr);
            Veiculo veiculo = new Veiculo(modelo, placa, ano, marca, cor);
            
            // Verificar se a placa já existe
            Veiculo veiculoExistente = oficina.buscarVeiculoPorPlaca(placa);
            if (veiculoExistente != null) {
                JOptionPane.showMessageDialog(this, "Já existe um veículo com esta placa!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            cliente.adicionarVeiculo(veiculo);
            veiculoAdicionado = true;
            
            JOptionPane.showMessageDialog(this, "Veículo adicionado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ano deve ser um número válido!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar veículo: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isVeiculoAdicionado() {
        return veiculoAdicionado;
    }
} 