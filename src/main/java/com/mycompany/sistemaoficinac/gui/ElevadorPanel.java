package com.mycompany.sistemaoficinac.gui;

import com.mycompany.sistemaoficinac.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Painel para gerenciamento de elevadores.
 */
public class ElevadorPanel extends BasePanel {
    
    public ElevadorPanel(Oficina oficina) {
        super(oficina, "Gerenciamento de Elevadores");
    }
    
    @Override
    protected void setupTable() {
        String[] colunas = {"Número", "Status", "Veículo Atual", "Tempo de Uso"};
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
        buttonPanel.add(createButton("Listar Elevadores", e -> listarElevadores()));
        buttonPanel.add(createButton("Alocar Elevador", e -> alocarElevador()));
        buttonPanel.add(createButton("Liberar Elevador", e -> liberarElevador()));
    }
    
    @Override
    protected void setupForm() {
        // Formulário será implementado quando necessário
    }
    
    @Override
    protected void loadData() {
        tableModel.setRowCount(0);
        Elevador[] elevadores = oficina.getElevadores();
        java.time.LocalDateTime agora = java.time.LocalDateTime.now();
        for (int i = 0; i < elevadores.length; i++) {
            Elevador elevador = elevadores[i];
            String status = elevador.getEstado();
            String veiculo = elevador.getModelo();
            String tempoUso = "-";
            if (elevador.getHorarioAlocacao() != null && elevador.isOcupado()) {
                java.time.Duration dur = java.time.Duration.between(elevador.getHorarioAlocacao(), agora);
                long horas = dur.toHours();
                long minutos = dur.toMinutesPart();
                tempoUso = String.format("%dh %dm", horas, minutos);
            }
            tableModel.addRow(new Object[]{i+1, status, veiculo, tempoUso});
        }
    }
    
    @Override
    protected boolean saveFormData() {
        return true; // Implementar quando necessário
    }
    
    private void listarElevadores() {
        loadData();
        showMessage("Lista de elevadores atualizada!");
    }
    
    private void alocarElevador() {
        String numStr = JOptionPane.showInputDialog(this, "Número do elevador (1-3):", "Alocar Elevador", JOptionPane.QUESTION_MESSAGE);
        if (numStr == null) return;
        try {
            int num = Integer.parseInt(numStr.trim());
            if (num < 1 || num > 3) {
                showError("Número inválido!");
                return;
            }
            Elevador elevador = oficina.getElevadores()[num-1];
            if (!"Disponível".equalsIgnoreCase(elevador.getEstado())) {
                showError("Elevador não está disponível!");
                return;
            }
            String modelo = JOptionPane.showInputDialog(this, "Modelo do veículo:", "Alocar Elevador", JOptionPane.QUESTION_MESSAGE);
            if (modelo == null || modelo.trim().isEmpty()) {
                showError("Modelo não informado!");
                return;
            }
            elevador.setModelo(modelo.trim());
            elevador.setEstado("Ocupado");
            elevador.setHorarioAlocacao(java.time.LocalDateTime.now());
            elevador.adicionarRegistroHistorico("Alocado em " + java.time.LocalDateTime.now() + " para o veículo: " + modelo);
            loadData();
            showMessage("Elevador " + num + " alocado para o veículo: " + modelo);
        } catch (Exception ex) {
            showError("Valor inválido: " + ex.getMessage());
        }
    }
    
    private void liberarElevador() {
        String numStr = JOptionPane.showInputDialog(this, "Número do elevador (1-3):", "Liberar Elevador", JOptionPane.QUESTION_MESSAGE);
        if (numStr == null) return;
        try {
            int num = Integer.parseInt(numStr.trim());
            if (num < 1 || num > 3) {
                showError("Número inválido!");
                return;
            }
            Elevador elevador = oficina.getElevadores()[num-1];
            if (!"Ocupado".equalsIgnoreCase(elevador.getEstado())) {
                showError("Elevador já está livre!");
                return;
            }
            elevador.setEstado("Disponível");
            elevador.adicionarRegistroHistorico("Liberado em " + java.time.LocalDateTime.now());
            loadData();
            showMessage("Elevador " + num + " liberado!");
        } catch (Exception ex) {
            showError("Valor inválido: " + ex.getMessage());
        }
    }
} 