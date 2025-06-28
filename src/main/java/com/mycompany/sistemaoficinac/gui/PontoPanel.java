package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Painel para gerenciamento de ponto.
 */
public class PontoPanel extends BasePanel {
    
    public PontoPanel(Oficina oficina) {
        super(oficina, "Gerenciamento de Ponto");
    }
    
    @Override
    protected void setupTable() {
        String[] colunas = {"Funcionário", "Data", "Entrada", "Saída", "Total Horas"};
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
        buttonPanel.add(createButton("Listar Registros", e -> listarRegistros()));
    }
    
    @Override
    protected void setupForm() {
        // Formulário será implementado quando necessário
    }
    
    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        for (PontoFuncionario ponto : oficina.getRegistrosPonto()) {
            String nome = ponto.getMatricula();
            for (Funcionario f : oficina.getFuncionarios()) {
                if (f.getMatricula().equals(ponto.getMatricula())) {
                    nome = f.getNome();
                    break;
                }
            }
            String data = ponto.getEntrada() != null ? ponto.getEntrada().toLocalDate().toString() : "-";
            String entrada = ponto.getEntrada() != null ? ponto.getEntrada().toLocalTime().toString() : "-";
            String saida = ponto.getSaida() != null ? ponto.getSaida().toLocalTime().toString() : "-";
            String total = "-";
            if (ponto.getEntrada() != null && ponto.getSaida() != null) {
                java.time.Duration dur = java.time.Duration.between(ponto.getEntrada(), ponto.getSaida());
                long horas = dur.toHours();
                long minutos = dur.toMinutesPart();
                total = String.format("%dh %dm", horas, minutos);
            }
            tableModel.addRow(new Object[]{nome, data, entrada, saida, total});
        }
    }
    
    @Override
    protected boolean saveFormData() {
        return true; // Implementar quando necessário
    }
    
    private void registrarEntrada() {
        String matricula = JOptionPane.showInputDialog(this, "Matrícula do funcionário:", "Registrar Entrada", JOptionPane.QUESTION_MESSAGE);
        if (matricula == null || matricula.trim().isEmpty()) return;
        try {
            boolean existe = oficina.getFuncionarios().stream().anyMatch(f -> f.getMatricula().equals(matricula.trim()));
            if (!existe) {
                showError("Funcionário não encontrado!");
                return;
            }
            boolean jaEntrou = oficina.getRegistrosPonto().stream().anyMatch(p -> p.getMatricula().equals(matricula.trim()) && p.getSaida() == null);
            if (jaEntrou) {
                showError("Funcionário já registrou entrada e não registrou saída!");
                return;
            }
            oficina.getRegistrosPonto().add(new PontoFuncionario(matricula.trim(), java.time.LocalDateTime.now(), null));
            loadData();
            showMessage("Entrada registrada!");
        } catch (Exception ex) {
            showError("Erro ao registrar entrada: " + ex.getMessage());
        }
    }
    
    private void registrarSaida() {
        String matricula = JOptionPane.showInputDialog(this, "Matrícula do funcionário:", "Registrar Saída", JOptionPane.QUESTION_MESSAGE);
        if (matricula == null || matricula.trim().isEmpty()) return;
        try {
            boolean encontrou = false;
            for (int i = oficina.getRegistrosPonto().size() - 1; i >= 0; i--) {
                PontoFuncionario ponto = oficina.getRegistrosPonto().get(i);
                if (ponto.getMatricula().equals(matricula.trim()) && ponto.getSaida() == null) {
                    ponto.setSaida(java.time.LocalDateTime.now());
                    encontrou = true;
                    break;
                }
            }
            if (!encontrou) {
                showError("Não foi encontrado registro de entrada para esta matrícula!");
                return;
            }
            loadData();
            showMessage("Saída registrada!");
        } catch (Exception ex) {
            showError("Erro ao registrar saída: " + ex.getMessage());
        }
    }
    
    private void listarRegistros() {
        loadData();
        showMessage("Lista de registros de ponto atualizada!");
    }
} 