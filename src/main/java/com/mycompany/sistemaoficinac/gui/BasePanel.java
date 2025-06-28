package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.Oficina;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe base para todos os painéis da interface Swing.
 * Fornece funcionalidades comuns como layout padrão, tabelas e botões.
 */
public abstract class BasePanel extends JPanel {
    
    protected final Oficina oficina;
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JPanel buttonPanel;
    protected JPanel formPanel;
    protected JScrollPane scrollPane;
    
    public BasePanel(Oficina oficina, String title) {
        this.oficina = oficina;
        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 50));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        initializeComponents(title);
        setupLayout();
        loadData();
    }
    
    protected void initializeComponents(String title) {
        // Título
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Tabela
        setupTable();
        scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(40, 40, 50));
        table.setBackground(new Color(60, 63, 65));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(80, 80, 100));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(100, 100, 120));
        table.setFont(table.getFont().deriveFont(12f));
        table.getTableHeader().setBackground(new Color(50, 50, 60));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD, 12f));
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botões
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(40, 40, 50));
        setupButtons();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Painel de formulário (inicialmente oculto)
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(40, 40, 50));
        setupForm();
    }
    
    protected void setupLayout() {
        // Pode ser customizado pelas subclasses
    }
    
    protected abstract void setupTable();
    
    protected abstract void setupButtons();
    
    protected abstract void setupForm();
    
    protected abstract void loadData();
    
    public void atualizarDados() {
        loadData();
    }
    
    protected JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 16f));
        button.setBackground(new Color(30, 120, 220));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 255), 2),
            BorderFactory.createEmptyBorder(10, 24, 10, 24)
        ));
        button.addActionListener(action);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(20, 90, 180));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(30, 120, 220));
            }
        });
        
        return button;
    }
    
    protected void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    protected boolean confirmAction(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    protected void showFormDialog(JFrame parent, String title) {
        JDialog formFrame = new JDialog(parent, title, true);
        formFrame.setLayout(new BorderLayout());
        formFrame.add(formPanel, BorderLayout.CENTER);
        
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formButtonPanel.setBackground(new Color(40, 40, 50));
        formButtonPanel.add(createButton("Salvar", e -> {
            if (saveFormData()) {
                formFrame.dispose();
                loadData();
            }
        }));
        formButtonPanel.add(createButton("Cancelar", e -> formFrame.dispose()));
        
        formFrame.add(formButtonPanel, BorderLayout.SOUTH);
        formFrame.pack();
        formFrame.setLocationRelativeTo(parent);
        formFrame.setVisible(true);
    }
    
    protected abstract boolean saveFormData();
    
    protected void clearForm() {
        // Implementação padrão vazia - subclasses podem sobrescrever
    }
    
    protected int getSelectedRowIndex() {
        return table.getSelectedRow();
    }
    
    protected boolean hasSelection() {
        return getSelectedRowIndex() >= 0;
    }
    
    protected void addFormField(JPanel panel, String labelText, Component field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel label = new JLabel(labelText + ":");
        label.setForeground(Color.WHITE);
        label.setFont(label.getFont().deriveFont(14f));
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }
    
    protected JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(field.getFont().deriveFont(14f));
        return field;
    }
    
    protected JPasswordField createPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(field.getFont().deriveFont(14f));
        return field;
    }
    
    protected JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(comboBox.getFont().deriveFont(14f));
        return comboBox;
    }
    
    protected JTextArea createTextArea(int rows, int columns) {
        JTextArea textArea = new JTextArea(rows, columns);
        textArea.setFont(textArea.getFont().deriveFont(14f));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }
} 