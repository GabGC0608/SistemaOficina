package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.Login;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Diálogo de login para o sistema de oficina.
 */
public class LoginDialog extends JDialog {
    
    private JTextField usuarioField;
    private JPasswordField senhaField;
    private JComboBox<String> tipoUsuarioComboBox;
    private boolean loginSucesso = false;
    private String tipoUsuario = null;
    private final Login loginManager;
    
    public LoginDialog(JFrame parent, Login loginManager) {
        super(parent, "Login - Sistema Oficina C", true);
        this.loginManager = loginManager;
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Título
        JLabel titleLabel = new JLabel("Sistema de Login", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(titleLabel, BorderLayout.NORTH);
        
        // Painel do formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        int linha = 0;
        
        // Tipo de Usuário
        gbc.gridx = 0;
        gbc.gridy = linha;
        formPanel.add(new JLabel("Tipo de Usuário:"), gbc);
        
        tipoUsuarioComboBox = new JComboBox<>(new String[]{"Funcionario", "Administrador"});
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(tipoUsuarioComboBox, gbc);
        
        linha++;
        
        // Usuário
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Usuário:"), gbc);
        
        usuarioField = new JTextField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(usuarioField, gbc);
        
        linha++;
        
        // Senha
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Senha:"), gbc);
        
        senhaField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(senhaField, gbc);
        
        // Informações de login padrão
        linha++;
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel infoLabel = new JLabel("Login padrão: admin/admin123 ou func/func123");
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.ITALIC, 11f));
        infoLabel.setForeground(Color.GRAY);
        formPanel.add(infoLabel, gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JButton loginButton = new JButton("Entrar");
        JButton cancelButton = new JButton("Cancelar");
        
        loginButton.addActionListener(e -> realizarLogin());
        cancelButton.addActionListener(e -> {
            loginSucesso = false;
            dispose();
        });
        
        // Configurar botão padrão
        getRootPane().setDefaultButton(loginButton);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Adicionar listener para Enter no campo de senha
        senhaField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    realizarLogin();
                }
            }
        });
    }
    
    private void setupLayout() {
        pack();
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        // Centralizar na tela
        setLocationRelativeTo(null);
    }
    
    private void realizarLogin() {
        String usuario = usuarioField.getText().trim();
        String senha = new String(senhaField.getPassword());
        String tipoSelecionado = (String) tipoUsuarioComboBox.getSelectedItem();
        
        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, preencha usuário e senha.",
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            tipoUsuario = loginManager.login(usuario, senha);
            
            if (tipoUsuario != null) {
                if (tipoUsuario.equals(tipoSelecionado)) {
                    loginSucesso = true;
                    JOptionPane.showMessageDialog(this,
                        "Login bem-sucedido! Tipo de usuário: " + tipoUsuario,
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Tipo de usuário incorreto. Selecione: " + tipoUsuario,
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    tipoUsuarioComboBox.setSelectedItem(tipoUsuario);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Login falhou! Usuário ou senha inválidos.",
                    "Erro de Login", JOptionPane.ERROR_MESSAGE);
                senhaField.setText("");
                usuarioField.selectAll();
                usuarioField.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao realizar login: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public boolean isLoginSucesso() {
        return loginSucesso;
    }
    
    public String getTipoUsuario() {
        return tipoUsuario;
    }
} 